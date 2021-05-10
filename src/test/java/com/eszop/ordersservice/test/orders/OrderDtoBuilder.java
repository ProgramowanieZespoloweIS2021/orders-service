package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;

public class OrderDtoBuilder {

    private Long buyerId = 1L;
    private Long offerId = 1L;
    private Long tierId = 1L;
    private String description = "description";

    public OrderDtoBuilder setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    public OrderDtoBuilder setOfferId(Long offerId) {
        this.offerId = offerId;
        return this;
    }

    public OrderDtoBuilder setTierId(Long tierId) {
        this.tierId = tierId;
        return this;
    }

    public OrderDtoBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public OrderDto build() {
        return new OrderDto(buyerId, offerId, tierId, description);
    }

}
