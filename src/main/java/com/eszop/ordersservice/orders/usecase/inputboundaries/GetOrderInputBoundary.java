package com.eszop.ordersservice.orders.usecase.inputboundaries;

import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.exception.OrdersServiceException;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;

import java.util.List;
import java.util.Set;

import static java.text.MessageFormat.format;

public interface GetOrderInputBoundary {

    Order byId(Long id);

    List<Order> byQueryCriteria(QueryCriteriaCollection queryCriteriaCollection);

    Set<Order> all();

    class OrderNotFoundException extends OrdersServiceException {
        public OrderNotFoundException(Long orderId) {
            super(format("Order with id ({0}) does not exist", orderId));
        }
    }
}
