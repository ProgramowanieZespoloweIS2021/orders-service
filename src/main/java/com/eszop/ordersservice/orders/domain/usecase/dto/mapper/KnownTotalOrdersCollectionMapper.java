package com.eszop.ordersservice.orders.domain.usecase.dto.mapper;

import com.eszop.ordersservice.orders.data.externalapi.OffersApiClient;
import com.eszop.ordersservice.orders.data.externalapi.UsersApiClient;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.KnownTotalCollection;
import com.eszop.ordersservice.orders.domain.usecase.dto.UserDto;
import com.eszop.ordersservice.orders.presentation.views.response.GetOrderResponse;
import com.eszop.ordersservice.orders.presentation.views.response.KnownTotalGetOrdersResponse;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class KnownTotalOrdersCollectionMapper {

    public static KnownTotalGetOrdersResponse toResponse(KnownTotalCollection<Order> orders, UsersApiClient usersApiClient, OffersApiClient offersApiClient) {
       Map<Order, UserDto> sellerByOrder = usersApiClient.getSellerByOrder(new ArrayList<>(orders.items()));
       Map<Order, UserDto> buyerByOrder = usersApiClient.getBuyerByOrder(new ArrayList<>(orders.items()));
       Map<Order, Object> offerByOrder = offersApiClient.getOfferByOrder(new ArrayList<>(orders.items()));

        return new KnownTotalGetOrdersResponse(orders.items().stream().map(
                order -> new GetOrderResponse(
                        offerByOrder.get(order),
                        buyerByOrder.get(order),
                        sellerByOrder.get(order),
                        order.getTierId(),
                        order
                )
        ).collect(Collectors.toList()), orders.total());
    }

}
