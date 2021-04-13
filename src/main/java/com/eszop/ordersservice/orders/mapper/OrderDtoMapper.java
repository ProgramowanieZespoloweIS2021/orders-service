package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.Order;

public class OrderDtoMapper {

    public static Order toOrder(PostOrderRequest postOrderRequest){
        var toReturn = new Order();
        toReturn.setId(postOrderRequest.id);
        toReturn.setOfferId(postOrderRequest.offerId);
        toReturn.setBuyerId(postOrderRequest.buyerId);
        toReturn.setTierId(postOrderRequest.tierId);
        toReturn.setDescription(postOrderRequest.description);
        toReturn.setState(postOrderRequest.state);
        toReturn.setCreationDate(postOrderRequest.creationDate);
        return toReturn;
    }

}
