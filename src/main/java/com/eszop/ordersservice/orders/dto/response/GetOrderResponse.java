package com.eszop.ordersservice.orders.dto.response;

import com.eszop.ordersservice.orders.entity.OrderState;

import java.util.Date;

public class GetOrderResponse {

    public Object offer;
    public Object buyer;
    public Object selectedTier;
    public Long id;
    public String description;
    public OrderState state;
    public Date creationDate;

    public GetOrderResponse(Object offer, Object buyer, Object selectedTier, Long id, String description, OrderState orderState, Date creationDate) {
        this.offer = offer;
        this.buyer = buyer;
        this.selectedTier = selectedTier;
        this.id = id;
        this.description = description;
        this.state = orderState;
        this.creationDate = creationDate;
    }

}
