package com.eszop.ordersservice.orders.repository;

import com.eszop.ordersservice.orders.dao.OrderDao;
import com.eszop.ordersservice.orders.usecase.datagateways.GetOrderDataSourceGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class JpaGetOrder implements GetOrderDataSourceGateway {

    private final JpaOrderRepository jpaOrderRepository;

    public JpaGetOrder(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Optional<OrderDao> byId(Long id) {
        return jpaOrderRepository.findById(id);
    }

    @Override
    public Set<OrderDao> all() {
        return null;
    }
}
