package com.eszop.ordersservice.orders.repository;

import com.eszop.ordersservice.orders.dao.OrderDao;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.usecase.datagateways.CreateOrderDataSourceGateway;
import org.springframework.stereotype.Component;

@Component
public class JpaCreateOrder implements CreateOrderDataSourceGateway {

    private final JpaOrderRepository jpaOrderRepository;

    public JpaCreateOrder(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void create(Order order) {
        jpaOrderRepository.save(OrderDao.from(order));
    }
}
