package com.eszop.ordersservice.orders.domain.usecase.dto;

import com.eszop.ordersservice.orders.domain.entity.OrderState;

import java.time.LocalDateTime;

public class OrderDto {

    public Long id;
    public Long buyerId;
    public Long offerId;
    public Long tierId;
    public String description;
    public OrderState state;
    public LocalDateTime creationDate;

    public OrderDto() {
    }

    public OrderDto(Long id, Long buyerId, Long offerId, Long tierId, String description, OrderState state, LocalDateTime creationDate) {
        this.id = id;
        this.buyerId = buyerId;
        this.offerId = offerId;
        this.tierId = tierId;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
    }
}
