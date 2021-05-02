package com.eszop.ordersservice.querycriteria;

import java.util.Collections;
import java.util.List;

public class QueryCriteriaCollection {

    private List<FilterCriteria<?>> filterCriteria = Collections.emptyList();
    private List<OrderCriteria> orderingCriteria = Collections.emptyList();
    private PaginationCriteria paginationCriteria;

    public QueryCriteriaCollection() {
    }

    public QueryCriteriaCollection(List<FilterCriteria<?>> filterCriteria, List<OrderCriteria> orderingCriteria, PaginationCriteria paginationCriteria) {
        this.filterCriteria = filterCriteria;
        this.orderingCriteria = orderingCriteria;
        this.paginationCriteria = paginationCriteria;
    }

    public List<FilterCriteria<?>> getFilterQueryCriteria() {
        return filterCriteria;
    }

    public QueryCriteriaCollection setFilterQueryCriteria(List<FilterCriteria<?>> filterCriteria) {
        this.filterCriteria = filterCriteria;
        return this;
    }

    public PaginationCriteria getPaginationQueryCriteria() {
        return paginationCriteria;
    }

    public QueryCriteriaCollection setPaginationQueryCriteria(PaginationCriteria paginationCriteria) {
        this.paginationCriteria = paginationCriteria;
        return this;
    }

    public List<OrderCriteria> getOrderingQueryCriteria() {
        return orderingCriteria;
    }

    public QueryCriteriaCollection setOrderingQueryCriteria(List<OrderCriteria> orderCriteria) {
        this.orderingCriteria = orderCriteria;
        return this;
    }
}
