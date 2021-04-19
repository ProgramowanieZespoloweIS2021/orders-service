package com.eszop.ordersservice.orders.domain.usecase.inputboundaries;

import com.eszop.ordersservice.orders.domain.usecase.dto.OrderStateDto;

public interface UpdateOrderStateInputBoundary {

    void updateState(OrderStateDto orderStateDto, Long id);

}
