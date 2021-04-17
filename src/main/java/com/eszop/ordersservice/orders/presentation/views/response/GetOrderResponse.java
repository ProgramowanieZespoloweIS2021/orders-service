package com.eszop.ordersservice.orders.presentation.views.response;

import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.entity.OrderState;

import java.time.LocalDateTime;

public class GetOrderResponse {

    public Object offer;
    public Object buyer;
    public Object selectedTier;
    public Long id;
    public LocalDateTime creationDate;
    public String description;
    public OrderState state;

    public GetOrderResponse(Object offer, Object buyer, Object selectedTier, OrderDto orderDto) {
        this.offer = offer;
        this.buyer = buyer;
        this.selectedTier = selectedTier;
        this.id = orderDto.id;
        this.creationDate = orderDto.creationDate;
        this.description = orderDto.description;
        this.state = orderDto.state;
    }

}
