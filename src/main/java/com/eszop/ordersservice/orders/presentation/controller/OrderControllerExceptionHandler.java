package com.eszop.ordersservice.orders.presentation.controller;

import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class OrderControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = OrdersServiceException.class)
    public ResponseEntity<?> handleException(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                Map.of("errorMessage", exception.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request
        );
    }
}
