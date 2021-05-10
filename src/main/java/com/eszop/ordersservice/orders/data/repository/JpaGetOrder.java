package com.eszop.ordersservice.orders.data.repository;

import com.eszop.ordersservice.orders.data.orm.OrderOrm;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.GetOrderDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.KnownTotalCollection;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class JpaGetOrder implements GetOrderDataSourceGateway {

    private final JpaOrderRepository jpaOrderRepository;
    private final EntityManager entityManager;

    public JpaGetOrder(JpaOrderRepository jpaOrderRepository, EntityManager entityManager) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<OrderOrm> byId(Long id) {
        return jpaOrderRepository.findById(id);
    }

    @Override
    public KnownTotalCollection<OrderOrm> all(QueryCriteriaCollection queryCriteriaCollection) {
        return new QueryCriteriaApplicatorJpa<>(entityManager, OrderOrm.class).apply(queryCriteriaCollection);
    }

    @Override
    public Set<OrderOrm> all() {
        return new HashSet<>(jpaOrderRepository.findAll());
    }
}
