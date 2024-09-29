package ru.draen.hps.datagen.service.generators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.draen.hps.app.client.dao.ClientRepository;
import ru.draen.hps.app.client.dao.ClientSpecification;
import ru.draen.hps.domain.Client;
import ru.draen.hps.domain.Operator;

@Component
@AllArgsConstructor
public class ClientGenerator implements IGenerator<Operator, Client> {
    private final IGenerator<Void, String> phoneNumberGenerator;
    private final ClientRepository clientRepository;

    @Override
    public Client generate(Operator operator) {
        Client client = new Client();
        // рудиментарная генерация уникальных номеров
        do {
            client.setPhoneNumber(phoneNumberGenerator.generate(null));
        } while (clientRepository.exists(ClientSpecification.byPhoneNumber(client.getPhoneNumber())));
        client.setOperator(operator);
        return client;
    }
}
