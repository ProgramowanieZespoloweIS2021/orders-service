package com.eszop.ordersservice.orders.repository;

import com.eszop.ordersservice.querycriteria.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryCriteriaApplicatorJpa<T> {

    private final EntityManager entityManager;
    private final Class<T> typeArgument;
    FilteringCriteriaApplicator filteringApplicator = new FilteringCriteriaApplicator();
    OrderingCriteriaApplicator sortingApplicator = new OrderingCriteriaApplicator();


    public QueryCriteriaApplicatorJpa(EntityManager entityManager, Class<T> typeArgument) {
        this.entityManager = entityManager;
        this.typeArgument = typeArgument;
    }

    public List<T> apply(QueryCriteriaCollection queryCriteriaCollection) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(typeArgument);
        Root<T> root = criteriaQuery.from(typeArgument);
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();


        for (var filterQueryCriteria : queryCriteriaCollection.getFilterQueryCriteria()) {
            predicates.add(filteringApplicator.apply(criteriaBuilder, root, filterQueryCriteria));
        }

        for (var sortQueryCriteria : queryCriteriaCollection.getSortQueryCriteria()) {
            orders.add(sortingApplicator.apply(criteriaBuilder, root, sortQueryCriteria));
        }

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        criteriaQuery.orderBy(orders);


        var typedQuery = entityManager.createQuery(criteriaQuery);
        PaginationCriteria paginationCriteria = queryCriteriaCollection.getPaginationQueryCriteria();
        typedQuery.setFirstResult(paginationCriteria.getOffset());
        typedQuery.setMaxResults(paginationCriteria.getLimit());

        return typedQuery.getResultList();
    }


    private static class FilteringCriteriaApplicator {
        Map<FilterType, TriFunction<CriteriaBuilder, Root<?>, FilterCriteria<? extends Comparable>, Predicate>> mappingByFilterType = Map.of(
                FilterType.EQUAL, (criteriaBuilder, root, filterCriteria) -> criteriaBuilder.equal(root.get(filterCriteria.getFieldName()), filterCriteria.getValue()),
                FilterType.LESS, (criteriaBuilder, root, filterCriteria) -> criteriaBuilder.lessThan(root.get(filterCriteria.getFieldName()), filterCriteria.getValue()),
                FilterType.LESS_EQUAL, (criteriaBuilder, root, filterCriteria) -> criteriaBuilder.lessThanOrEqualTo(root.get(filterCriteria.getFieldName()), filterCriteria.getValue()),
                FilterType.GREATER, (criteriaBuilder, root, filterCriteria) -> criteriaBuilder.greaterThan(root.get(filterCriteria.getFieldName()), filterCriteria.getValue()),
                FilterType.GREATER_EQUAL, (criteriaBuilder, root, filterCriteria) -> criteriaBuilder.greaterThanOrEqualTo(root.get(filterCriteria.getFieldName()), filterCriteria.getValue())
        );

        Predicate apply(CriteriaBuilder criteriaBuilder, Root<?> root, FilterCriteria filterCriteria) {
            return mappingByFilterType.get(filterCriteria.getFilterType()).apply(criteriaBuilder, root, filterCriteria);
        }
    }

    private static class OrderingCriteriaApplicator {

        Map<OrderType, TriFunction<CriteriaBuilder, Root<?>, OrderCriteria, Order>> mappingByFilterType = Map.of(
                OrderType.ASCENDING, (criteriaBuilder, root, orderCriteria) -> criteriaBuilder.asc(root.get(orderCriteria.getFieldName())),
                OrderType.DESCENDING, (criteriaBuilder, root, orderCriteria) -> criteriaBuilder.desc(root.get(orderCriteria.getFieldName()))
        );

        Order apply(CriteriaBuilder criteriaBuilder, Root<?> root, OrderCriteria orderCriteria) {
            return mappingByFilterType.get(orderCriteria.getSortType()).apply(criteriaBuilder, root, orderCriteria);
        }
    }

}
