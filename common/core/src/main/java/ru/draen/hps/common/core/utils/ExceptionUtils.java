package ru.draen.hps.common.core.utils;

import java.util.concurrent.Callable;

public class ExceptionUtils {

    @FunctionalInterface
    public interface RunnableWithException {
        void run() throws Exception;
    }

    public static<T> T wrapChecked(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void wrapChecked(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
