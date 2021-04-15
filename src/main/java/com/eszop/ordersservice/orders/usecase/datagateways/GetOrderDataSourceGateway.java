package com.eszop.ordersservice.orders.usecase.datagateways;

import com.eszop.ordersservice.orders.orm.OrderOrm;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GetOrderDataSourceGateway {

    Optional<OrderOrm> byId(Long id);

    List<OrderOrm> all(QueryCriteriaCollection queryCriteriaCollection);

    Set<OrderOrm> all();

}
