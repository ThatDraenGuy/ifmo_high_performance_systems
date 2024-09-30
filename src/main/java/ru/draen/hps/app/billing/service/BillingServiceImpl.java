package ru.draen.hps.app.billing.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.app.cdrdata.dao.CdrDataRepository;
import ru.draen.hps.app.cdrdata.dao.CdrDataSpecification;
import ru.draen.hps.app.cdrfile.dao.CdrFileRepository;
import ru.draen.hps.app.report.dao.ReportRepository;
import ru.draen.hps.app.tariff.dao.TariffHistRepository;
import ru.draen.hps.app.tariff.dao.TariffHistSpecification;
import ru.draen.hps.common.exception.NotFoundException;
import ru.draen.hps.common.model.StreamCondition;
import ru.draen.hps.domain.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class BillingServiceImpl implements BillingService {
    private final TransactionTemplate transactionTemplate;
    private final CdrFileRepository cdrFileRepository;
    private final CdrDataRepository cdrDataRepository;
    private final TariffHistRepository tariffHistRepository;
    private final ReportRepository reportRepository;

    @Override
    public void perform(BillingRequest request) {
        transactionTemplate.execute(status -> {
            CdrFile cdrFile = cdrFileRepository.findById(request.cdrFileId()).orElseThrow(NotFoundException::new);
            cdrFileRepository.getClients(cdrFile).forEach(client -> {
                Report report = processClient(cdrFile, client);
                reportRepository.save(report);
            });
            return null;
        });
    }

    private Report processClient(CdrFile cdrFile, Client client) {
        Stream<CdrData> calls = cdrDataRepository.findStream(CdrDataSpecification.byClient(cdrFile, client),
                new StreamCondition(0, Sort.by(CdrData_.START_TIME)));

        Report report = new Report();
        report.setOperator(cdrFile.getFile().getOperator());
        report.setClient(client);
        report.setStartTime(cdrFile.getStartTime());
        report.setEndTime(cdrFile.getEndTime());
        report.setTotalMinutes(0);

        TariffHist tariffHist = tariffHistRepository.findOne(TariffHistSpecification.byTariff(client.getTariff(), cdrFile.getStartTime()))
                .orElseThrow(NotFoundException::new);
        List<TariffRule> tariffRules = tariffHist.getOrderedRules();

        calls.forEach(call -> processCall(report, tariffRules, call));
        return report;
    }

    private void processCall(Report report, List<TariffRule> rules, CdrData call) {
        Duration callDuration = Duration.between(call.getStartTime(), call.getEndTime());
        call.setMinutes((int) callDuration.toMinutes() + 1);

        int previousMinutes = report.getTotalMinutes();
        int callMinutes = call.getMinutes();

        for (TariffRule rule : rules) {
            int cutoffMinutes = rule.getMinuteLimit();
            int minutesLeft = cutoffMinutes - previousMinutes;

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

        if (callMinutes != 0) throw new IllegalStateException(); //TODO tariffHist rules exhausted

        report.addTotalMinutes(call.getMinutes());
        report.addTotalCost(call.getCost());
    }
}
