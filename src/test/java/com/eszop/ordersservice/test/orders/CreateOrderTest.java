package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.dto.request.TierDto;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.entity.OrderState;
import com.eszop.ordersservice.orders.usecase.CreateOrder;
import com.eszop.ordersservice.orders.usecase.datagateways.CreateOrderDataSourceGateway;
import com.eszop.ordersservice.orders.usecase.inputboundaries.CreateOrderInputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CreateOrderTest {

    @Mock
    CreateOrderDataSourceGateway createOrderDataSourceGateway;

    private CreateOrder sut;

    @BeforeEach
    public void setUp() {
        createOrderDataSourceGateway = mock(CreateOrderDataSourceGateway.class);
        doNothing().when(createOrderDataSourceGateway).create(Mockito.any(Order.class));
        sut = new CreateOrder(createOrderDataSourceGateway);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, ORDERED",
            "1, 2, 1, FINISHED"
    })
    public void Valid_order_is_created(Long id, Long offerId, Long selectedTierId, OrderState orderState) throws CreateOrderInputBoundary.SelectedOfferDoesNotExistException, CreateOrderInputBoundary.TierDoesNotExistForSelectedOfferException {
        PostOrderRequest order = new OrderDtoBuilder().setId(id).setOfferId(offerId).setTierId(selectedTierId).setState(orderState).build();
        OfferDto offer = new OfferDtoBuilder().setId(offerId).build();

        sut.create(order, offer);

        verify(createOrderDataSourceGateway, times(1)).create(Mockito.any(Order.class));
    }

    @Test
    public void Create_order_with_invalid_tierId_fails() {
        PostOrderRequest order = new OrderDtoBuilder().setTierId(12L).build();
        OfferDto offer = new OfferDtoBuilder().setTiers(Set.of(new TierDto(1L), new TierDto(2L))).build();

        assertThatThrownBy(() -> sut.create(order, offer))
                .isInstanceOf(CreateOrderInputBoundary.TierDoesNotExistForSelectedOfferException.class);
    }

    @Test
    public void Create_order_with_invalid_offerId_fails() {
        PostOrderRequest order = new OrderDtoBuilder().setOfferId(2L).build();
        OfferDto offer = new OfferDtoBuilder().setId(null).build();

        assertThatThrownBy(() -> sut.create(order, offer))
                .isInstanceOf(CreateOrderInputBoundary.SelectedOfferDoesNotExistException.class);
    }

}
