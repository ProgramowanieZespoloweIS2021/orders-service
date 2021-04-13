package com.eszop.ordersservice.querycriteria;

import java.util.Collections;
import java.util.List;

public class QueryCriteriaCollection{

    private List<FilterQueryCriteria<?>> filterQueryCriteria = Collections.emptyList();
    private List<SortQueryCriteria> sortQueryCriteria = Collections.emptyList();
    private PaginationQueryCriteria paginationQueryCriteria;

    public QueryCriteriaCollection setFilterQueryCriteria(List<FilterQueryCriteria<?>> filterQueryCriteria) {
        this.filterQueryCriteria = filterQueryCriteria;
        return this;
    }

    public QueryCriteriaCollection setSortQueryCriteria(List<SortQueryCriteria> sortQueryCriteria) {
        this.sortQueryCriteria = sortQueryCriteria;
        return this;
    }

    public QueryCriteriaCollection setPaginationQueryCriteria(PaginationQueryCriteria paginationQueryCriteria) {
        this.paginationQueryCriteria = paginationQueryCriteria;
        return this;
    }

    public List<FilterQueryCriteria<?>> getFilterQueryCriteria() {
        return filterQueryCriteria;
    }

    public PaginationQueryCriteria getPaginationQueryCriteria() {
        return paginationQueryCriteria;
    }

    public List<SortQueryCriteria> getSortQueryCriteria() {
        return sortQueryCriteria;
    }
}
