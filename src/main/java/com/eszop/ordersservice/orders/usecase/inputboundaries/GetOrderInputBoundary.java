package com.eszop.ordersservice.orders.usecase.inputboundaries;

import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.usecase.ComparableAndQueryCriteriaCollection;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;

import java.util.List;
import java.util.Set;

import static java.text.MessageFormat.format;

public interface GetOrderInputBoundary {

    Order byId(Long id) throws OrderNotFoundException;
    List<Order> byQueryCriteria(ComparableAndQueryCriteriaCollection queryCriteriaCollection);
    Set<Order> all();

    class OrderNotFoundException extends Exception{

        private final String message;

        public OrderNotFoundException(Long orderId){
            this.message = format("Order with id ({0}) does not exist", orderId);
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
