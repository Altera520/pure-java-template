package org.altera.util;

@FunctionalInterface
public interface UncheckBiConsumer<T, R, E extends Exception> {
    void call(T t1, R t2) throws E;
}