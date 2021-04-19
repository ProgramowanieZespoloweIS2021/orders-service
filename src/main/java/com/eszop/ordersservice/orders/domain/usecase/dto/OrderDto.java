package com.eszop.ordersservice.orders.domain.usecase.dto;

import com.eszop.ordersservice.orders.domain.entity.OrderState;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class OrderDto {

    public Long id;
    @NotNull
    public Long buyerId;
    @NotNull
    public Long offerId;
    @NotNull
    public Long tierId;
    @NotEmpty
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
