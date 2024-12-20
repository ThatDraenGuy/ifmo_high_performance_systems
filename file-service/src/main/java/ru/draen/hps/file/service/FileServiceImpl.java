package ru.draen.hps.file.service;

import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.jpadao.transaction.StreamTransactionTemplate;
import ru.draen.hps.file.controller.dto.FileCondition;
import ru.draen.hps.file.dao.FileFetchProfile;
import ru.draen.hps.file.dao.FileRepository;
import ru.draen.hps.file.dao.FileSpecification;
import ru.draen.hps.common.jpadao.dao.EntityLoader;
import ru.draen.hps.common.dbms.domain.File;

import java.util.Optional;

import static ru.draen.hps.common.webflux.utils.CacheUtils.cacheDelete;
import static ru.draen.hps.common.webflux.utils.CacheUtils.cacheGet;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final StreamTransactionTemplate streamTransactionTemplate;
    private final TransactionTemplate transactionTemplate;
    private final FileRepository fileRepository;
    private final EntityLoader entityLoader;
    private final IMap<Long, File> fileCache;

    @Override
    public Flux<File> findAll(@NonNull FileCondition condition) {
        return Flux.fromStream(streamTransactionTemplate.execute(status -> {
            Specification<File> spec = FileSpecification.getByCondition(condition);
            return fileRepository.findStream(spec);
        }));
    }

    @Override
    public Mono<File> getWithContent(@NonNull Long id) {
        return cacheGet(fileCache, id, () -> Mono.justOrEmpty(readOnlyTransactionTemplate.execute(status ->
                fileRepository.findById(id, FileFetchProfile::withContent))));
    }

    @Override
    public Mono<File> create(@NonNull File file) {
        return Mono.justOrEmpty(Optional.ofNullable(transactionTemplate.execute(status -> {
            prepareToCreate(file);
            return fileRepository.saveWithContent(file);
        })));
    }

    @Override
    public Mono<Boolean> delete(@NonNull Long id) {
        return cacheDelete(fileCache, id, () -> Mono.fromSupplier(() ->
                Boolean.TRUE.equals(transactionTemplate.execute(status -> fileRepository.delete(id)))));
    }

    private void prepareToCreate(File file) {
        file.setOperator(entityLoader.load(file.getOperator()));
    }
}
