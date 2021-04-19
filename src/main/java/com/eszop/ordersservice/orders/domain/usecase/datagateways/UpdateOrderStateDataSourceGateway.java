package com.eszop.ordersservice.orders.domain.usecase.datagateways;

import com.eszop.ordersservice.orders.domain.entity.OrderState;

public interface UpdateOrderStateDataSourceGateway {

    void update(Long id, OrderState orderState);

    boolean existsById(Long id);

}
