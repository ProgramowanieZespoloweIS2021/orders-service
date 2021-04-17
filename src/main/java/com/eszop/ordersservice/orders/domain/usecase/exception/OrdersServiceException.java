package com.eszop.ordersservice.orders.domain.usecase.exception;

public class OrdersServiceException extends RuntimeException {
    public OrdersServiceException(String message) {
        super(message);
    }
}
