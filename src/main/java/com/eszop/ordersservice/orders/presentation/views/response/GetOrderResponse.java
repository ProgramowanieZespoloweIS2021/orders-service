package com.eszop.ordersservice.orders.presentation.views.response;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;

import java.time.LocalDateTime;

public class GetOrderResponse {

    public Object offer;
    public Object buyer;
    public Object seller;
    public Object selectedTierId;
    public Long id;
    public LocalDateTime creationDate;
    public String description;
    public OrderState state;

    public GetOrderResponse(Object offer, Object buyer, Object seller, Long selectedTierId, Order order) {
        this.offer = offer;
        this.buyer = buyer;
        this.seller = seller;
        this.selectedTierId = selectedTierId;
        this.id = order.getId();
        this.creationDate = order.getCreationDate();
        this.description = order.getDescription();
        this.state = order.getState();
    }

}
