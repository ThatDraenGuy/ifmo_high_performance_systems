package ru.draen.hps.app.file.dao;

import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.File;
import ru.draen.hps.domain.File_;

import java.util.List;

@Repository
@AllArgsConstructor
public class FileRepositoryImpl extends ADeletableRepository<File, Long> implements FileRepository {
    private final FileContentRepository fileContentRepository;

    @Override
    public File save(File entity) {
        entityManager.persist(entity);
        entity.getContent().setId(entity.getId());
        fileContentRepository.save(entity.getContent());
        return entity;
    }

    @Override
    protected @NonNull Class<File> getEntityClass() {
        return File.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<File> root) {
        FileFetchProfile.brief(root);
    }

    @Override
    protected @NonNull List<File> modifyFindResult(@NonNull List<File> foundEntities) {
        foundEntities.forEach(file -> {
            if (Persistence.getPersistenceUtil().isLoaded(file, File_.CONTENT)) {
                fileContentRepository.fetchFileBlob(file.getContent());
            }
        });
        return foundEntities;
    }
}
