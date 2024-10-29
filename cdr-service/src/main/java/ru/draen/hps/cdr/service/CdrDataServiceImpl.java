package ru.draen.hps.cdr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.dao.CdrDataRepository;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;


@Service
@AllArgsConstructor
public class CdrDataServiceImpl implements CdrDataService {
    private final CdrDataRepository cdrDataRepository;

    @Override
    @Transactional
    public Flux<CdrData> findByClient(Long fileId, Long clientId) {
        return cdrDataRepository.findByClient(fileId, clientId);
    }

    @Override
    @Transactional
    public Flux<CdrData> save(Flux<CdrData> calls) {
        return cdrDataRepository.saveAll(calls);
    }
}
