package ru.draen.hps.datagen.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.cdrdata.service.csv.CdrDataItem;
import ru.draen.hps.app.client.dao.ClientRepository;
import ru.draen.hps.app.operator.dao.OperatorRepository;
import ru.draen.hps.common.exception.NotFoundException;
import ru.draen.hps.common.utils.ExceptionUtils;
import ru.draen.hps.datagen.controller.dto.CdrGenerationRequest;
import ru.draen.hps.datagen.controller.dto.CdrGenerationResponse;
import ru.draen.hps.datagen.controller.dto.ClientGenerationRequest;
import ru.draen.hps.datagen.service.generators.IGenerator;
import ru.draen.hps.domain.Client;
import ru.draen.hps.domain.Operator;

import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DatagenServiceImpl implements DatagenService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TransactionTemplate transactionTemplate;
    private final IGenerator<Operator, Client> clientGenerator;
    private final IGenerator<CdrGenerationRequest, CdrDataItem> cdrDataItemGenerator;
    private final ClientRepository clientRepository;
    private final OperatorRepository operatorRepository;

    @Override
    public Long generateClients(ClientGenerationRequest request) {
        return transactionTemplate.execute(status -> {
            Operator operator = operatorRepository.findById(request.getOperatorId()).orElseThrow(NotFoundException::new);
            for (long index = 0; index < request.getCount(); index++) {
                Client client = clientGenerator.generate(operator);
                clientRepository.save(client);
            }
            return request.getCount();
        });
    }

    @Override
    public CdrGenerationResponse generateCdr(CdrGenerationRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return readOnlyTransactionTemplate.execute(status -> {
            Operator operator = operatorRepository.findById(request.getOperatorId()).orElseThrow(NotFoundException::new);
            List<CdrDataItem> calls = new ArrayList<>();
            for (long index = 0; index < request.getCallsCount(); index++) {
                calls.add(cdrDataItemGenerator.generate(request));
            }

            String fileName = "cdr_"
                    + operator.getCode()
                    + "_" + request.getStartDate().format(formatter)
                    + "_" + request.getEndDate().format(formatter)
                    + ".csv";
            java.io.File file = new java.io.File("files", fileName);
            ExceptionUtils.wrapChecked(() -> {
                try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                    StatefulBeanToCsv<CdrDataItem> beanToCsv = new StatefulBeanToCsvBuilder<CdrDataItem>(writer)
                            .withApplyQuotesToAll(false)
                            .build();
                    beanToCsv.write(calls);
                }
            });
            return new CdrGenerationResponse(fileName);
        });
    }
}
