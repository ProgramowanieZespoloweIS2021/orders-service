package com.eszop.ordersservice.orders.domain.usecase;

import com.eszop.ordersservice.orders.domain.usecase.datagateways.UpdateOrderStateDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderStateDto;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.GetOrderInputBoundary;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.UpdateOrderStateInputBoundary;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UpdateOrderState implements UpdateOrderStateInputBoundary {

    private final UpdateOrderStateDataSourceGateway dataSourceGateway;

    public UpdateOrderState(UpdateOrderStateDataSourceGateway dataSourceGateway) {
        this.dataSourceGateway = dataSourceGateway;
    }

    @Override
    public void updateState(OrderStateDto orderStateDto, Long id) {
        if (!dataSourceGateway.existsById(id)) {
            throw new GetOrderInputBoundary.OrderNotFoundException(id);
        }
        dataSourceGateway.update(id, orderStateDto.value);
    }
}
