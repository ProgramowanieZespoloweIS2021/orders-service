package com.eszop.ordersservice.orders.domain.usecase.dto.mapper;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;

public class OrderDtoMapper {

    public static Order toOrder(OrderDto orderDto) {
        return new Order(orderDto.buyerId,
                orderDto.offerId, orderDto.tierId, orderDto.description);
    }

}
