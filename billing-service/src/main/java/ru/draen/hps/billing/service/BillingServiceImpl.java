package ru.draen.hps.billing.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.I18n;
import ru.draen.hps.billing.client.CdrFileClient;
import ru.draen.hps.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.dao.ReportRepository;
import ru.draen.hps.billing.dao.TariffHistRepository;
import ru.draen.hps.billing.dao.TariffRuleRepository;
import ru.draen.hps.common.core.exception.ProcessingException;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.r2dbcdao.domain.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class BillingServiceImpl implements BillingService {
    private final ILabelService lbs = I18n.getLabelService();

    private final ReportRepository reportRepository;
    private final TariffHistRepository tariffHistRepository;
    private final TariffRuleRepository tariffRuleRepository;
    private final CdrFileClient cdrFileClient;

    @Override
    @Transactional
    public Mono<Void> perform(BillingRequest request) {
        return cdrFileClient.findById(request.cdrFileId()).flatMapMany(cdrFile ->
                cdrFileClient.findClients(cdrFile.getId()).flatMap(client -> {
                    Mono<Report> report = processClient(cdrFile, client);
                    return reportRepository.saveAll(report);
                })).then();
    }

    private Mono<Report> processClient(CdrFile cdrFile, Client client) {
        return tariffHistRepository.findByTariff(client.getTariffId(), cdrFile.getStartTime())
                .flatMapMany(tariffHist -> tariffRuleRepository.findOrderedRules(tariffHist.getTariffId()))
                .collectList()
                .flatMap(rules -> {
                    Flux<CdrData> calls = cdrFileClient.findClientRecords(cdrFile.getId(), client.getId());
                    return calls.collect(() -> {
                        Report report = new Report();
//                        report.setOperatorId(cdrFile.getOperatorId());
                        report.setClientId(client.getId());
                        report.setStartTime(cdrFile.getStartTime());
                        report.setEndTime(cdrFile.getEndTime());
                        report.setTotalMinutes(0);
                        return report;
                    }, (report, call) -> processCall(report, rules, call));
                });
    }

    private void processCall(Report report, List<TariffRule> rules, CdrData call) {
        Duration callDuration = Duration.between(call.getStartTime(), call.getEndTime());
        call.setMinutes((int) callDuration.toMinutes() + 1);

        int previousMinutes = report.getTotalMinutes();
        int callMinutes = call.getMinutes();

        for (TariffRule rule : rules) {
            Integer cutoffMinutes = rule.getMinuteLimit();
            int minutesLeft = isNull(cutoffMinutes) ? Integer.MAX_VALUE : cutoffMinutes - previousMinutes;

            if (minutesLeft < 0) {
                // это правило уже "израсходовано"
                // идём к следующему, уменьшив число "использованных" минут
                previousMinutes -= minutesLeft;
            } else if (minutesLeft < callMinutes) {
                // это правило покрывает звонок частично
                // тарифицируем входящую часть и идём к следующему правило, сбрасывая до нуля число "использованных" минут
                call.addToCost(rule.getMinuteCost().multiply(BigDecimal.valueOf(minutesLeft)));
                callMinutes -= minutesLeft;
                previousMinutes = 0;
            } else {
                // правило полностью покрывает звонок
                // тарифицируем не думая и не смотрим на дальнейшие правила
                call.addToCost(rule.getMinuteCost().multiply(BigDecimal.valueOf(callMinutes)));
                callMinutes = 0;
                break;
            }
        }

        if (callMinutes != 0) throw new ProcessingException(lbs.msg("ProcessingException.BillingService.tariffRulesExhausted"));

        report.addTotalMinutes(call.getMinutes());
        report.addTotalCost(call.getCost());
    }
}
