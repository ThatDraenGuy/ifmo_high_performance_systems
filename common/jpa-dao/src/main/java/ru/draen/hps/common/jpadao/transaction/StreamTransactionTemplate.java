package ru.draen.hps.common.jpadao.transaction;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Slf4j
@AllArgsConstructor
public class StreamTransactionTemplate extends DefaultTransactionDefinition {
    private PlatformTransactionManager transactionManager;

    public <T> Stream<T> execute(@NonNull StreamableTransactionCallback<T> action) throws TransactionException {
        Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");

        TransactionStatus status = this.transactionManager.getTransaction(this);
        Stream<T> result;
        try {
            result = action.doInTransaction(status);
        }
        catch (RuntimeException | Error ex) {
            // Transactional code threw application exception -> rollback
            rollbackOnException(status, ex);
            throw ex;
        }
        catch (Throwable ex) {
            // Transactional code threw unexpected exception -> rollback
            rollbackOnException(status, ex);
            throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
        }
        if (!isNull(result))
            result = result.onClose(() -> this.transactionManager.commit(status));
        return result;
    }

    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");

        log.debug("Initiating transaction rollback on application exception", ex);
        try {
            this.transactionManager.rollback(status);
        }
        catch (TransactionSystemException ex2) {
            log.error("Application exception overridden by rollback exception", ex);
            ex2.initApplicationException(ex);
            throw ex2;
        }
        catch (RuntimeException | Error ex2) {
            log.error("Application exception overridden by rollback exception", ex);
            throw ex2;
        }
    }
}
