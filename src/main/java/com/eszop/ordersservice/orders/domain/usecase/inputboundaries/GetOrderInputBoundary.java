package com.eszop.ordersservice.orders.domain.usecase.inputboundaries;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.KnownTotalCollection;
import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;

import java.util.Set;

import static java.text.MessageFormat.format;

public interface GetOrderInputBoundary {

    Order byId(Long id);

    KnownTotalCollection<Order> byQueryCriteria(QueryCriteriaCollection queryCriteriaCollection);

    Set<Order> all();

    class OrderNotFoundException extends OrdersServiceException {
        public OrderNotFoundException(Long orderId) {
            super(format("Order with id ({0}) does not exist", orderId));
        }
    }
}
