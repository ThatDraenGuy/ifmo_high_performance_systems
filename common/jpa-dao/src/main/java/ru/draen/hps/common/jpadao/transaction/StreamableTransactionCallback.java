package ru.draen.hps.common.jpadao.transaction;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.stream.Stream;

@FunctionalInterface
public interface StreamableTransactionCallback<T> extends TransactionCallback<Stream<T>> {
    @Override
    Stream<T> doInTransaction(TransactionStatus status);
}
