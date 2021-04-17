package com.eszop.ordersservice.orders.data.repository;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.data.orm.OrderOrm;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.CreateOrderDataSourceGateway;
import org.springframework.stereotype.Component;

@Component
public class JpaCreateOrder implements CreateOrderDataSourceGateway {

    private final JpaOrderRepository jpaOrderRepository;

    public JpaCreateOrder(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void create(Order order) {
        jpaOrderRepository.save(OrderOrm.from(order));
    }
}
