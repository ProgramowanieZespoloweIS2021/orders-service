package com.eszop.ordersservice.orders.usecase.datagateways;

import com.eszop.ordersservice.orders.entity.Order;

public interface CreateOrderDataSourceGateway {

    void create(Order order);

}
