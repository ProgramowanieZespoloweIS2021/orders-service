package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.domain.usecase.CreateOrder;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.CreateOrderDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.TierDto;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.CreateOrderInputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CreateOrderTest {

    @Mock
    CreateOrderDataSourceGateway createOrderDataSourceGateway;

    private CreateOrder sut;

    @BeforeEach
    public void setUp() {
        createOrderDataSourceGateway = mock(CreateOrderDataSourceGateway.class);
        doNothing().when(createOrderDataSourceGateway).create(any(Order.class));
        sut = new CreateOrder(createOrderDataSourceGateway);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, ORDERED",
            "1, 2, 1, FINISHED"
    })
    public void Valid_order_is_created(Long id, Long offerId, Long selectedTierId, OrderState orderState) throws CreateOrderInputBoundary.SelectedOfferDoesNotExistException, CreateOrderInputBoundary.TierDoesNotExistForSelectedOfferException {
        OrderDto order = new OrderDtoBuilder().setId(id).setOfferId(offerId).setTierId(selectedTierId).setState(orderState).build();
        OfferDto offer = new OfferDtoBuilder().setId(offerId).build();

        sut.create(order, offer);

        verify(createOrderDataSourceGateway, times(1)).create(any(Order.class));
    }

    @Test
    public void Create_order_with_invalid_tierId_fails() {
        OrderDto order = new OrderDtoBuilder().setTierId(12L).build();
        OfferDto offer = new OfferDtoBuilder().setTiers(List.of(new TierDto(1L), new TierDto(2L))).build();

        assertThatThrownBy(() -> sut.create(order, offer))
                .isInstanceOf(CreateOrderInputBoundary.TierDoesNotExistForSelectedOfferException.class);
    }

    @Test
    public void Create_order_with_invalid_offerId_fails() {
        OrderDto order = new OrderDtoBuilder().setOfferId(2L).build();
        OfferDto offer = new OfferDtoBuilder().setId(null).build();

        assertThatThrownBy(() -> sut.create(order, offer))
                .isInstanceOf(CreateOrderInputBoundary.SelectedOfferDoesNotExistException.class);
    }

}
