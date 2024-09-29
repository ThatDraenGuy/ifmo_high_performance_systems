package ru.draen.hps.datagen.service.generators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.draen.hps.app.cdrdata.service.csv.CdrDataItem;
import ru.draen.hps.app.client.controller.dto.ClientCondition;
import ru.draen.hps.app.client.service.ClientService;
import ru.draen.hps.datagen.controller.dto.CdrGenerationRequest;
import ru.draen.hps.domain.Client;
import ru.draen.hps.domain.ECallDirection;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@AllArgsConstructor
public class CdrDataItemGenerator implements IGenerator<CdrGenerationRequest, CdrDataItem> {
    private final static long MAX_CALL_SECONDS = 1800;
    private final ClientService clientService;
    private final IGenerator<Period, LocalDateTime> dateTimeGenerator;
    private final Random random = new Random();

    @Override
    public CdrDataItem generate(CdrGenerationRequest request) {
        Client client = clientService.findRandom(new ClientCondition(null, request.getOperatorId()));
        LocalDateTime startTime = dateTimeGenerator.generate(
                new Period(request.getStartDate().atStartOfDay(),
                        request.getEndDate().atStartOfDay().minusSeconds(MAX_CALL_SECONDS)));
        LocalDateTime endTime = dateTimeGenerator.generate(
                new Period(startTime, startTime.plusSeconds(MAX_CALL_SECONDS)));
        return new CdrDataItem(
                random.nextBoolean() ? ECallDirection.INC : ECallDirection.OUT,
                client.getPhoneNumber(),
                startTime,
                endTime
        );
    }
}
