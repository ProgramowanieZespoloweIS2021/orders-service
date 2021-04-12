package com.eszop.ordersservice.orders.usecase.datagateways;

import com.eszop.ordersservice.orders.dao.OrderDao;


import java.util.Optional;
import java.util.Set;

public interface GetOrderDataSourceGateway {

    Optional<OrderDao> byId(Long id);
    Set<OrderDao> all();

}
