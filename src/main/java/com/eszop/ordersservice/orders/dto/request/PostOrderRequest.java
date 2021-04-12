package com.eszop.ordersservice.orders.dto.request;

import com.eszop.ordersservice.orders.entity.OrderState;

import java.util.Date;

public class PostOrderRequest {

    public Long id;
    public Long buyerId;
    public Long offerId;
    public Long tierId;
    public String description;
    public OrderState state;
    public Date creationDate;

    public PostOrderRequest() {
    }

    public PostOrderRequest(Long id, Long buyerId, Long offerId, Long tierId,
                            String description, OrderState state, Date creationDate) {
        this.id = id;
        this.buyerId = buyerId;
        this.offerId = offerId;
        this.tierId = tierId;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
    }
}
