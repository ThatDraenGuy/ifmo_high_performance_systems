package ru.draen.hps.billing.app.billing.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import ru.draen.hps.billing.I18n;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.report.service.ReportService;
import ru.draen.hps.billing.app.tariff.dao.TariffHistSpecification;
import ru.draen.hps.billing.app.tariff.service.TariffService;
import ru.draen.hps.billing.client.CdrFileClient;
import ru.draen.hps.billing.common.model.CdrDataModel;
import ru.draen.hps.billing.common.model.CdrFileModel;
import ru.draen.hps.billing.common.model.ClientModel;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.exception.ProcessingException;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.dbms.domain.*;
import ru.draen.hps.common.jpadao.entity.IEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class BillingServiceImpl implements BillingService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final CdrFileClient cdrFileClient;
    private final TariffService tariffService;
    private final ReportService reportService;

    @Override
    @Transactional
    public Mono<Void> perform(BillingRequest request) {
        Flux<Report> reports = cdrFileClient
                .findById(request.cdrFileId())
                .switchIfEmpty(Mono.error(NotFoundException::new))
                .flatMapMany(cdrFile -> cdrFileClient.findClients(cdrFile.getFileId())
                        .map(client -> Tuples.of(cdrFile, client)))
                .flatMap(tuple -> processClient(tuple.getT1(), tuple.getT2()));
        return reportService.save(reports).then();
    }

    private Mono<Report> processClient(CdrFileModel cdrFile, ClientModel client) {
        return tariffService.findOne(TariffHistSpecification.byTariff(client.getTariffId(), TimestampHelper.toInstant(cdrFile.getStartDate())))
                .switchIfEmpty(Mono.error(NotFoundException::new))
                .flatMap(tariffHist -> {
                    AtomicInteger totalMinutes = new AtomicInteger(0);
                    Flux<CdrDataModel> processedCalls = cdrFileClient.findClientRecords(cdrFile.getFileId(), client.getClientId())
                            .map(call -> processCall(totalMinutes, tariffHist.getOrderedRules(), call));
                    return processedCalls.flatMap(call -> cdrFileClient.updateRecord(cdrFile.getFileId(), call)).reduceWith(
                            () -> {
                                Report report = new Report();
                                report.setOperator(IEntity.mapId(cdrFile.getFile().getOperatorId(), Operator::new));
                                report.setClient(IEntity.mapId(client.getClientId(), Client::new));
                                report.setStartTime(TimestampHelper.toInstant(cdrFile.getStartDate()));
                                report.setEndTime(TimestampHelper.toInstant(cdrFile.getEndDate()));
                                report.setTotalMinutes(0);
                                return report;
                            },
                            (report, call) -> {
                                report = report.addTotalMinutes(call.getMinutes());
                                report = report.addTotalCost(call.getCost());
                                return report;
                            });
                });
    }

    private CdrDataModel processCall(AtomicInteger totalMinutes, List<TariffRule> rules, CdrDataModel call) {
        Duration callDuration = Duration.between(call.getStartTime(), call.getEndTime());
        call = call.withMinutes((int) callDuration.toMinutes() + 1);

        int previousMinutes = totalMinutes.getAndAdd(call.getMinutes());
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
                call = call.addToCost(rule.getMinuteCost().multiply(BigDecimal.valueOf(minutesLeft)));
                callMinutes -= minutesLeft;
                previousMinutes = 0;
            } else {
                // правило полностью покрывает звонок
                // тарифицируем не думая и не смотрим на дальнейшие правила
                call = call.addToCost(rule.getMinuteCost().multiply(BigDecimal.valueOf(callMinutes)));
                callMinutes = 0;
                break;
            }
        }

        if (callMinutes != 0) throw new ProcessingException(lbs.msg("ProcessingException.BillingService.tariffRulesExhausted"));
        return call;
    }
}
