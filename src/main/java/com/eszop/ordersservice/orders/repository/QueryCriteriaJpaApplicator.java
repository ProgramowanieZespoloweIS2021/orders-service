package com.eszop.ordersservice.orders.repository;

import com.eszop.ordersservice.querycriteria.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

public class QueryCriteriaJpaApplicator<T> {

    private final EntityManager entityManager;
    private final Class<T> typeArgument;

    public QueryCriteriaJpaApplicator(EntityManager entityManager, Class<T> typeArgument) {
        this.entityManager = entityManager;
        this.typeArgument = typeArgument;
    }


    public Set<T> apply(QueryCriteriaCollection queryCriteriaCollection) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(typeArgument);
        Root<T> root = criteriaQuery.from(typeArgument);


        List<Predicate> predicates = new ArrayList<>();
        List<FilterQueryCriteria<?>> filterQueryCriteriaCollection = queryCriteriaCollection.getFilterQueryCriteria();
        FilterQueryCriteriaApplicator filterApplicator = new FilterQueryCriteriaApplicator();
        if (!filterQueryCriteriaCollection.isEmpty()) {
            for (var filterQueryCriteria : filterQueryCriteriaCollection) {
                predicates.add(filterApplicator.applyFilterQueryCriteria(criteriaBuilder, root, filterQueryCriteria));
            }
        }

        List<Order> orders = new ArrayList<>();
        List<SortQueryCriteria> sortQueryCriteriaCollection = queryCriteriaCollection.getSortQueryCriteria();
        SortQueryCriteriaApplicator sortApplicator = new SortQueryCriteriaApplicator();
        if (!sortQueryCriteriaCollection.isEmpty()) {
            for (var sortQueryCriteria : sortQueryCriteriaCollection) {
                orders.add(sortApplicator.applySortQueryCriteria(criteriaBuilder, root, sortQueryCriteria));
            }
        }

        PaginationQueryCriteria paginationQueryCriteria = queryCriteriaCollection.getPaginationQueryCriteria();

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        criteriaQuery.orderBy(orders);

        var typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(paginationQueryCriteria.getOffset());
        typedQuery.setMaxResults(paginationQueryCriteria.getLimit());

        return new HashSet<>(typedQuery.getResultList());
    }


    private class FilterQueryCriteriaApplicator {

        Map<FilterType, TriFunction<CriteriaBuilder, Root<?>, FilterQueryCriteria<?>, Predicate>> mappingByFilterType = Map.of(
                FilterType.EQUAL, (criteriaBuilder, root, filterQueryCriteria) -> criteriaBuilder.equal(root.get(filterQueryCriteria.getFieldName()), filterQueryCriteria.getValue())
        );

        Predicate applyFilterQueryCriteria(CriteriaBuilder criteriaBuilder, Root<?> root, FilterQueryCriteria<?> filterQueryCriteria) {
            return mappingByFilterType.get(filterQueryCriteria.getFilterType()).apply(criteriaBuilder, root, filterQueryCriteria);
        }
    }

    private class SortQueryCriteriaApplicator {

        Map<SortType, TriFunction<CriteriaBuilder, Root<?>, SortQueryCriteria, Order>> mappingByFilterType = Map.of(
                SortType.ASCENDING, (criteriaBuilder, root, sortQueryCriteria) -> criteriaBuilder.asc(root.get(sortQueryCriteria.getFieldName())),
                SortType.DESCENDING, (criteriaBuilder, root, sortQueryCriteria) -> criteriaBuilder.desc(root.get(sortQueryCriteria.getFieldName()))
        );

        Order applySortQueryCriteria(CriteriaBuilder criteriaBuilder, Root<?> root, SortQueryCriteria sortQueryCriteria) {
            return mappingByFilterType.get(sortQueryCriteria.getSortType()).apply(criteriaBuilder, root, sortQueryCriteria);
        }
    }

}
