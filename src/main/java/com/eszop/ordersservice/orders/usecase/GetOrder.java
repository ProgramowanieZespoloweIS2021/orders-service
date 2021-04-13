package com.eszop.ordersservice.orders.usecase;

import com.eszop.ordersservice.orders.dao.OrderDao;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.usecase.datagateways.GetOrderDataSourceGateway;
import com.eszop.ordersservice.orders.usecase.inputboundaries.GetOrderInputBoundary;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetOrder implements GetOrderInputBoundary {

    private final GetOrderDataSourceGateway getOrderDataSourceGateway;

    public GetOrder(GetOrderDataSourceGateway getOrderDataSourceGateway) {
        this.getOrderDataSourceGateway = getOrderDataSourceGateway;
    }

    @Override
    public Order byId(Long id) throws OrderNotFoundException {
        return getOrderDataSourceGateway.byId(id).orElseThrow(() -> new OrderNotFoundException(id)).asOrder();
    }

    @Override
    public List<Order> byQueryCriteria(ComparableAndQueryCriteriaCollection queryCriteriaCollection) {
        return getOrderDataSourceGateway.all(queryCriteriaCollection).stream().map(OrderDao::asOrder).collect(Collectors.toList());
    }

    @Override
    public Set<Order> all() {
        return getOrderDataSourceGateway.all().stream().map(OrderDao::asOrder).collect(Collectors.toSet());
    }

}
