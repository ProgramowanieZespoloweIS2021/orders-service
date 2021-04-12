package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.OrderState;

import java.util.Date;

public class OrderDtoBuilder {

    private Long id = 1L;
    private Long buyerId = 1L;
    private Long offerId = 1L;
    private Long tierId = 1L;
    private String description = "description";
    private OrderState state = OrderState.ORDERED;
    private Date creationDate = new Date();

    public OrderDtoBuilder setId(Long id) {
        this.id = id;
        return this;
    }

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

    public OrderDtoBuilder setState(OrderState state) {
        this.state = state;
        return this;
    }

    public OrderDtoBuilder setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public PostOrderRequest build(){
        return new PostOrderRequest(id, buyerId, offerId, tierId, description, state, creationDate);
    }


}
