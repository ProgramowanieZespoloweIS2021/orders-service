package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.mapper.OrderMapper;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.typeCompatibleWith;

public class OrderTest {

    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, 1, I want blah blah blah..., ORDERED",
            "2, 3, 2, 45, I want blah blah blah..., FINISHED"
    })
    public void Can_create_order(Long id, Long offerId, Long tierId, Long buyerId, String description, OrderState state){

        var sut = OrderMapper.toOrder(new OrderDto(id, buyerId, offerId, tierId, description, state, LocalDateTime.now()));

        assertThat(sut.getClass(), is(typeCompatibleWith(Order.class)));
        assertThat(sut.getOfferId(), is(equalTo(offerId)));
        assertThat(sut.getTierId(), is(equalTo(tierId)));
        assertThat(sut.getBuyerId(), is(equalTo(buyerId)));
        assertThat(sut.getDescription(), is(equalTo(description)));
        assertThat(sut.getState(), is(equalTo(state)));
    }

}
