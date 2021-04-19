package com.eszop.ordersservice.orders.data.repository;

import com.eszop.ordersservice.orders.data.orm.OrderOrm;
import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.UpdateOrderStateDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.GetOrderInputBoundary;
import org.springframework.stereotype.Component;

@Component
public class JpaUpdateOrderState implements UpdateOrderStateDataSourceGateway {

    private final JpaOrderRepository jpaOrderRepository;

    public JpaUpdateOrderState(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void update(Long id, OrderState orderState) {
        OrderOrm orderOrm = jpaOrderRepository.findById(id).orElseThrow(() -> new GetOrderInputBoundary.OrderNotFoundException(id));
        orderOrm.setState(orderState);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaOrderRepository.existsById(id);
    }


}
