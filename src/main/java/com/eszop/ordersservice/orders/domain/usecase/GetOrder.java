package com.eszop.ordersservice.orders.domain.usecase;

import com.eszop.ordersservice.orders.data.orm.OrderOrm;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.GetOrderDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.KnownTotalCollection;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.GetOrderInputBoundary;
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
    public Order byId(Long id) {
        return getOrderDataSourceGateway.byId(id).orElseThrow(() -> new OrderNotFoundException(id)).asOrder();
    }

    @Override
    public KnownTotalCollection<Order> byQueryCriteria(QueryCriteriaCollection queryCriteriaCollection) {
        var orderOrmKnownTotalCollection = getOrderDataSourceGateway.all(queryCriteriaCollection);
        return new KnownTotalCollection<>(orderOrmKnownTotalCollection.items().stream().map(OrderOrm::asOrder).collect(Collectors.toList()), orderOrmKnownTotalCollection.total());
    }

    @Override
    public Set<Order> all() {
        return getOrderDataSourceGateway.all().stream().map(OrderOrm::asOrder).collect(Collectors.toSet());
    }

}
