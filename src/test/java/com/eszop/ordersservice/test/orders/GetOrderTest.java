package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.dao.OrderDao;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.usecase.GetOrder;
import com.eszop.ordersservice.orders.usecase.datagateways.GetOrderDataSourceGateway;
import com.eszop.ordersservice.orders.usecase.inputboundaries.GetOrderInputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetOrderTest {

    private GetOrder sut;

    @Mock
    private GetOrderDataSourceGateway getOrderDataSourceGateway;

    @BeforeEach
    public void setUp(){
        getOrderDataSourceGateway = mock(GetOrderDataSourceGateway.class);
        this.sut = new GetOrder(getOrderDataSourceGateway);
    }

    @Test
    public void Getting_non_existing_order_fails(){
        when(getOrderDataSourceGateway.byId(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.byId(1L)).isInstanceOf(GetOrderInputBoundary.OrderNotFoundException.class);
    }

    @Test
    public void Getting_existing_order_succeeds(){
        when(getOrderDataSourceGateway.byId(1L)).thenReturn(Optional.of(OrderDao.from(new OrderBuilder().setId(1L).build())));
    }

    @Test
    public void Getting_all_orders_when_no_orders_registered_returns_empty_collection(){
        when(getOrderDataSourceGateway.all()).thenReturn(Collections.emptySet());

        var result = sut.all();
        assertTrue(result.isEmpty());
    }

    @Test
    public void Getting_all_orders_content_matches_actual_content(){
        OrderBuilder orderBuilder = new OrderBuilder();
        Order order1 = orderBuilder.setId(1L).build();
        Order order2 = orderBuilder.setId(2L).build();
        Order order3 = orderBuilder.setId(3L).build();
        when(getOrderDataSourceGateway.all()).thenReturn(Set.of(OrderDao.from(order1), OrderDao.from(order2), OrderDao.from(order3)));

        var result = sut.all();

        assertThat(result).containsExactlyInAnyOrder(order3, order2, order1);
    }

}
