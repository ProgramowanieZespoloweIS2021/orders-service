package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.domain.usecase.UpdateOrderState;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.UpdateOrderStateDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderStateDto;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.GetOrderInputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateOrderStateTest {

    UpdateOrderState sut;

    @Mock
    UpdateOrderStateDataSourceGateway dataSourceGateway;

    @BeforeEach
    public void setUp() {
        dataSourceGateway = mock(UpdateOrderStateDataSourceGateway.class);
        sut = new UpdateOrderState(dataSourceGateway);
    }

    @Test
    public void Can_update_order_state_for_existing_order(){
        doReturn(true).when(dataSourceGateway).existsById(any(Long.class));
        doNothing().when(dataSourceGateway).update(any(Long.class), any(OrderState.class));
        OrderStateDto orderStateDto = new OrderStateDto(){{
            value = OrderState.FINISHED;
        }};

        sut.updateState(orderStateDto, 1L);

        verify(dataSourceGateway, times(1)).update(1L, OrderState.FINISHED);
    }

    @Test
    public void Throws_exception_on_non_existing_order(){
        doReturn(false).when(dataSourceGateway).existsById(any(Long.class));
        OrderStateDto orderStateDto = new OrderStateDto(){{
            value = OrderState.FINISHED;
        }};

        assertThatThrownBy(() -> sut.updateState(orderStateDto, 1L)).isInstanceOf(GetOrderInputBoundary.OrderNotFoundException.class);
    }

}
