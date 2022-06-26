package org.altera.util;

@FunctionalInterface
public interface UncheckFunction<T, R, E extends Exception> {
    R call(T t) throws E;
}
