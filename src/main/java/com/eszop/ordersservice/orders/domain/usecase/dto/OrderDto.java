package com.eszop.ordersservice.orders.domain.usecase.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderDto {

    @NotNull
    public Long buyerId;
    @NotNull
    public Long offerId;
    @NotNull
    public Long tierId;
    @NotEmpty
    public String description;

    public OrderDto() {
    }

    public OrderDto(Long buyerId, Long offerId, Long tierId, String description) {
        this.buyerId = buyerId;
        this.offerId = offerId;
        this.tierId = tierId;
        this.description = description;
    }
}
