package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.Order;

public class OrderDtoMapper {

    public static Order toOrder(PostOrderRequest postOrderRequest) {
        return new Order(postOrderRequest.id, postOrderRequest.buyerId, postOrderRequest.offerId,
                postOrderRequest.tierId, postOrderRequest.description, postOrderRequest.state,
                postOrderRequest.creationDate);
    }

}
