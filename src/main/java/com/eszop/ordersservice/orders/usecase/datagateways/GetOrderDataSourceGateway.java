package com.eszop.ordersservice.orders.usecase.datagateways;

import com.eszop.ordersservice.orders.dao.OrderDao;
import com.eszop.ordersservice.orders.usecase.ComparableAndQueryCriteriaCollection;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GetOrderDataSourceGateway {

    Optional<OrderDao> byId(Long id);
    List<OrderDao> all(ComparableAndQueryCriteriaCollection queryCriteriaCollection);
    Set<OrderDao> all();

}
