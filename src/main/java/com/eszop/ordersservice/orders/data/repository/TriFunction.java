package com.eszop.ordersservice.orders.data.repository;

@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {
    R apply(T1 t, T2 t2, T3 t3);
}
