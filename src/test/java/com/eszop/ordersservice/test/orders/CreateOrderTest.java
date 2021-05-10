package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.entity.Order;
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
            "1, 1, 1, 2",
            "2, 1, 2, 1"
    })
    public void Valid_order_is_created(Long offerId, Long selectedTierId, Long buyerId, Long sellerId) {
        OrderDto order = new OrderDtoBuilder().setOfferId(offerId).setTierId(selectedTierId).setBuyerId(buyerId).build();
        OfferDto offer = new OfferDtoBuilder().setId(offerId).setSellerId(sellerId).build();

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
    public void Create_order_with_invalid_buyer_id_and_seller_id_fails() {
        OrderDto orderDto = new OrderDtoBuilder().setBuyerId(1L).build();
        OfferDto offerDto = new OfferDtoBuilder().setSellerId(1L).build();

        assertThatThrownBy(() -> sut.create(orderDto, offerDto))
                .isInstanceOf(CreateOrderInputBoundary.SellerIdAndBuyerIdAreNotValidException.class);
    }

}
