package ru.draen.hps.file.dao;

import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.jpadao.dao.ADeletableRepository;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.dbms.domain.FileContent;
import ru.draen.hps.common.dbms.domain.File_;

import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class FileRepositoryImpl extends ADeletableRepository<File, Long> implements FileRepository {
    private final FileContentRepository fileContentRepository;

    @Override
    public File saveWithContent(@NonNull File entity) {
        entityManager.persist(entity);
        fileContentRepository.save(entity.getContent());
        return entity;
    }

    @Override
    public File saveWithContentProvider(@NonNull File file, @NonNull Consumer<OutputStream> contentProvider) {
        entityManager.persist(file);
        FileContent fileContent = new FileContent();
        fileContent.setFile(file);
        file.setContent(fileContent);
        fileContentRepository.fillFileBlob(fileContent, contentProvider);
        entityManager.persist(fileContent);
        entityManager.flush();
        return file;
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
