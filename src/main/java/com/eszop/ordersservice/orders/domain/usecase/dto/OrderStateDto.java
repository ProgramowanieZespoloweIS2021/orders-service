package com.eszop.ordersservice.orders.domain.usecase.dto;

import com.eszop.ordersservice.orders.domain.entity.OrderState;

import javax.validation.constraints.NotNull;

public class OrderStateDto {

    @NotNull
    public OrderState value;

}
