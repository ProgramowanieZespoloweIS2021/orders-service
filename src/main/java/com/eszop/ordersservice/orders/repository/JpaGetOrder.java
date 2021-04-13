package com.eszop.ordersservice.orders.repository;

import com.eszop.ordersservice.orders.dao.OrderDao;
import com.eszop.ordersservice.orders.usecase.datagateways.GetOrderDataSourceGateway;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
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
    public Optional<OrderDao> byId(Long id) {
        return jpaOrderRepository.findById(id);
    }

    @Override
    public Set<OrderDao> all(QueryCriteriaCollection queryCriteriaCollection) {
        return new QueryCriteriaJpaApplicator<>(entityManager, OrderDao.class).apply(queryCriteriaCollection);
    }

    @Override
    public Set<OrderDao> all() {
        return null;
    }
}
