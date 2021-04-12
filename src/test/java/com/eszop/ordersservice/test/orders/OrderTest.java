package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.adapter.OrderDtoAdapter;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.entity.OrderState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Date;

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
    public void canCreateOrder(Long id, Long offerId, Long tierId, Long buyerId, String description, OrderState state){

        var sut = OrderDtoAdapter.toOrder(new PostOrderRequest(id, buyerId, offerId, tierId, description, state, new Date()));

        assertThat(sut.getClass(), is(typeCompatibleWith(Order.class)));
        assertThat(sut.getOfferId(), is(equalTo(offerId)));
        assertThat(sut.getTierId(), is(equalTo(tierId)));
        assertThat(sut.getBuyerId(), is(equalTo(buyerId)));
        assertThat(sut.getDescription(), is(equalTo(description)));
        assertThat(sut.getState(), is(equalTo(state)));
    }

}
