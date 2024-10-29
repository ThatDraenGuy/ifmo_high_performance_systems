package ru.draen.hps.file.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Repository;
import ru.draen.hps.file.I18n;
import ru.draen.hps.common.jpadao.dao.AGenericRepository;
import ru.draen.hps.common.jpadao.dao.FetchProfiles;
import ru.draen.hps.common.core.exception.AppException;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.file.utils.BlobUtils;
import ru.draen.hps.common.core.utils.ExceptionUtils;
import ru.draen.hps.common.dbms.domain.FileContent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

@Repository
public class FileContentRepositoryImpl extends AGenericRepository<FileContent, Long> implements FileContentRepository {
    private static final ILabelService lbs = I18n.getLabelService();

    @Override
    public <T> T processFileBlob(@NonNull FileContent fileContent, @NonNull Function<InputStream, ? extends T> blobProcessor) {
        return BlobUtils.readBlob(entityManager, fileContent.getDataId(), inputStream -> {
            CheckedInputStream crcStream = new CheckedInputStream(inputStream, new CRC32());
            T result = blobProcessor.apply(crcStream);
            validateChecksum(crcStream.getChecksum(), fileContent);
            return result;
        });
    }

    @Override
    public void fillFileBlob(@NonNull FileContent fileContent, @NonNull Consumer<OutputStream> blobProvider) {
        fileContent.setDataId(BlobUtils.writeBlob(entityManager, outputStream -> ExceptionUtils.wrapChecked(() -> {
            CheckedOutputStream crcStream = new CheckedOutputStream(outputStream, new CRC32());
            blobProvider.accept(crcStream);
            fileContent.setCheckSum(crcStream.getChecksum().getValue());
        })));
    }

    @Override
    public void fetchFileBlob(@NonNull FileContent fileContent) {
        fileContent.setData(processFileBlob(fileContent, inputStream ->
                ExceptionUtils.wrapChecked(() -> IOUtils.toByteArray(inputStream))));
    }

    @Override
    public FileContent save(@NonNull FileContent entity) {
        fillFileBlob(entity, outputStream ->
                ExceptionUtils.wrapChecked(() -> IOUtils.write(entity.getData(), outputStream)));
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    private void validateChecksum(Checksum checksum, FileContent fileContent) {
        if (!fileContent.getCheckSum().equals(checksum.getValue())) {
            throw new AppException(lbs.msg("AppException.fileContent.invalidChecksum", fileContent.getCheckSum(), checksum.getValue()));
        }
    }

    @Override
    protected @NonNull Class<FileContent> getEntityClass() {
        return FileContent.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<FileContent> root) {
        FetchProfiles.nothing(root);
    }
}
