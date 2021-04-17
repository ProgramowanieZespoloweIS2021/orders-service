package com.eszop.ordersservice.orders.domain.usecase.datagateways;

import com.eszop.ordersservice.orders.domain.entity.Order;

public interface CreateOrderDataSourceGateway {

    void create(Order order);

}
