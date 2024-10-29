package ru.draen.hps.file.utils;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlobUtils {
    public static <T> T readBlob(EntityManager entityManager, long dataId, Function<InputStream, ? extends T> blobProcessor) {
        return entityManager.unwrap(Session.class).doReturningWork(connection -> {
            LargeObjectManager manager = connection.unwrap(PGConnection.class).getLargeObjectAPI();
            try (LargeObject object = manager.open(dataId, LargeObjectManager.READ)) {
                return blobProcessor.apply(object.getInputStream());
            }
        });
    }

    public static long writeBlob(EntityManager entityManager, Consumer<OutputStream> blobProvider) {
        return entityManager.unwrap(Session.class).doReturningWork(connection -> {
            LargeObjectManager manager = connection.unwrap(PGConnection.class).getLargeObjectAPI();
            long dataId = manager.createLO();
            try (LargeObject object = manager.open(dataId)) {
                blobProvider.accept(object.getOutputStream());
            }
            return dataId;
        });
    }
}
