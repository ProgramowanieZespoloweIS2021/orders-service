package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;

import java.time.LocalDateTime;

public class OrderBuilder {

    private Long id = 1L;
    private Long buyerId = 1L;
    private Long sellerId = 1L;
    private Long offerId = 1L;
    private Long tierId = 1L;
    private String description = "description";
    private OrderState state = OrderState.ORDERED;

    public OrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    public OrderBuilder setOfferId(Long offerId) {
        this.offerId = offerId;
        return this;
    }

    public OrderBuilder setTierId(Long tierId) {
        this.tierId = tierId;
        return this;
    }

    public OrderBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public OrderBuilder setState(OrderState state) {
        this.state = state;
        return this;
    }

    public OrderBuilder setSellerId(Long sellerId){
        this.sellerId = sellerId;
        return this;
    }

    public Order build() {
        var built = new Order();
        built.setId(id);
        built.setBuyerId(buyerId);
        built.setSellerId(sellerId);
        built.setOfferId(offerId);
        built.setTierId(tierId);
        built.setDescription(description);
        built.setState(state);
        built.setCreationDate(LocalDateTime.now());
        return built;
    }


}
