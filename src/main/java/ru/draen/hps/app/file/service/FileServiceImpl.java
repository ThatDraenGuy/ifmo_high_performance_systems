package ru.draen.hps.app.file.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.file.controller.dto.FileCondition;
import ru.draen.hps.app.file.controller.dto.UploadLocalFileRequest;
import ru.draen.hps.app.file.dao.FileFetchProfile;
import ru.draen.hps.app.file.dao.FileRepository;
import ru.draen.hps.app.file.dao.FileSpecification;
import ru.draen.hps.common.dao.EntityLoader;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.exception.NotFoundException;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.common.utils.ExceptionUtils;
import ru.draen.hps.domain.File;
import ru.draen.hps.domain.Operator;

import java.io.FileInputStream;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TransactionTemplate transactionTemplate;
    private final FileRepository fileRepository;
    private final EntityLoader entityLoader;

    @Override
    public PageResponse<File> findAll(@NonNull FileCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<File> spec = FileSpecification.getByCondition(condition);
            return new PageResponse<>(
                    fileRepository.findAll(spec, pageCondition),
                    pageCondition,
                    fileRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<File> findAll(@NonNull FileCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<File> spec = FileSpecification.getByCondition(condition);
            return new ScrollResponse<>(
                    fileRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }

    @Override
    public Optional<File> getWithContent(Long fileId) {
        return readOnlyTransactionTemplate.execute(status ->
                fileRepository.findById(fileId, FileFetchProfile::withContent));
    }

    @Override
    public File createFromLocal(UploadLocalFileRequest request) {
        return transactionTemplate.execute(status -> {
            File file = new File();
            java.io.File localFile = new java.io.File(request.path());
            if (!localFile.isFile()) throw new NotFoundException();
            file.setFileName(localFile.getName());
            file.setOperator(entityLoader.load(IEntity.mapId(request.operatorId(), Operator::new)));
            prepareToCreate(file);
            return fileRepository.saveWithContentProvider(file, outputStream ->
                    ExceptionUtils.wrapChecked(() -> IOUtils.copy(new FileInputStream(localFile), outputStream)));
        });
    }

    @Override
    public File create(File file) {
        return transactionTemplate.execute(status -> {
            prepareToCreate(file);
            return fileRepository.saveWithContent(file);
        });
    }

    @Override
    public boolean delete(Long fileId) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> fileRepository.delete(fileId)));
    }

    private void prepareToCreate(File file) {
        file.setOperator(entityLoader.load(file.getOperator()));
    }
}
