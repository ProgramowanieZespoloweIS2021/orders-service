package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.OrderState;

import java.time.LocalDateTime;

public class PostOrderRequestBuilder {

    private Long id = 1L;
    private Long buyerId = 1L;
    private Long offerId = 1L;
    private Long tierId = 1L;
    private String description = "description";
    private OrderState state = OrderState.ORDERED;
    private LocalDateTime creationDate = LocalDateTime.now();

    public PostOrderRequestBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public PostOrderRequestBuilder setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    public PostOrderRequestBuilder setOfferId(Long offerId) {
        this.offerId = offerId;
        return this;
    }

    public PostOrderRequestBuilder setTierId(Long tierId) {
        this.tierId = tierId;
        return this;
    }

    public PostOrderRequestBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PostOrderRequestBuilder setState(OrderState state) {
        this.state = state;
        return this;
    }

    public PostOrderRequestBuilder setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public PostOrderRequest build(){
        return new PostOrderRequest(id, buyerId, offerId, tierId, description, state, creationDate);
    }


}
