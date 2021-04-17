package com.eszop.ordersservice.orders.domain.usecase.dto.mapper;

import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.entity.Order;

public class OrderMapper {

    public static Order toOrder(OrderDto orderDto) {
        return new Order(orderDto.id, orderDto.buyerId, orderDto.offerId,
                orderDto.tierId, orderDto.description, orderDto.state,
                orderDto.creationDate);
    }

    public static OrderDto toOrderDto(Order order){
        return new OrderDto(order.getId(), order.getBuyerId(), order.getOfferId(), order.getTierId(), order.getDescription(), order.getState(), order.getCreationDate());
    }

}
