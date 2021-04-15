package com.eszop.ordersservice.querycriteria;

import java.util.Collections;
import java.util.List;

public class QueryCriteriaCollection {

    private List<FilterCriteria<?>> filterCriteria = Collections.emptyList();
    private List<OrderCriteria> orderCriteria = Collections.emptyList();
    private PaginationCriteria paginationCriteria;

    public QueryCriteriaCollection() {
    }

    public QueryCriteriaCollection(List<FilterCriteria<?>> filterCriteria, List<OrderCriteria> orderCriteria, PaginationCriteria paginationCriteria) {
        this.filterCriteria = filterCriteria;
        this.orderCriteria = orderCriteria;
        this.paginationCriteria = paginationCriteria;
    }

    public QueryCriteriaCollection setFilterQueryCriteria(List<FilterCriteria<?>> filterCriteria) {
        this.filterCriteria = filterCriteria;
        return this;
    }

    public QueryCriteriaCollection setSortQueryCriteria(List<OrderCriteria> orderCriteria) {
        this.orderCriteria = orderCriteria;
        return this;
    }

    public QueryCriteriaCollection setPaginationQueryCriteria(PaginationCriteria paginationCriteria) {
        this.paginationCriteria = paginationCriteria;
        return this;
    }

    public List<FilterCriteria<?>> getFilterQueryCriteria() {
        return filterCriteria;
    }

    public PaginationCriteria getPaginationQueryCriteria() {
        return paginationCriteria;
    }

    public List<OrderCriteria> getSortQueryCriteria() {
        return orderCriteria;
    }
}
