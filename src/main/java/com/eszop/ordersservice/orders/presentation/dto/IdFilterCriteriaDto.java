package com.eszop.ordersservice.orders.presentation.dto;

import com.eszop.ordersservice.orders.domain.entity.OrderState;

import java.util.Optional;

public class IdFilterCriteriaDto {

    public Optional<Long> offerId;
    public Optional<Long> buyerId;
    public Optional<Long> sellerId;
    public Optional<Long> tierId;
    public Optional<OrderState> state;

    public IdFilterCriteriaDto() {
        this.offerId = Optional.empty();
        this.buyerId = Optional.empty();
        this.sellerId = Optional.empty();
        this.tierId = Optional.empty();
        this.state = Optional.empty();
    }

    public IdFilterCriteriaDto(Optional<Long> offerId, Optional<Long> buyerId, Optional<Long> sellerId, Optional<Long> tierId, Optional<OrderState> state) {
        this.offerId = offerId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.tierId = tierId;
        this.state = state;
    }
}
