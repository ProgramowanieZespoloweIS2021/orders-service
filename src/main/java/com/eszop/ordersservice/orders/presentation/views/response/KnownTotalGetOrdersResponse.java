package com.eszop.ordersservice.orders.presentation.views.response;

import java.util.List;

public class KnownTotalGetOrdersResponse {

    public List<GetOrderResponse> orders;
    public Long totalNumberOfOrders;

    public KnownTotalGetOrdersResponse(List<GetOrderResponse> orders, Long totalNumberOfOrders) {
        this.orders = orders;
        this.totalNumberOfOrders = totalNumberOfOrders;
    }

}
