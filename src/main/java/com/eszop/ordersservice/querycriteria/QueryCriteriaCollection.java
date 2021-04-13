package com.eszop.ordersservice.querycriteria;

import java.util.List;

public class QueryCriteriaCollection {

    private final List<FilterQueryCriteria<?>> filterQueryCriteria;
    private final List<SortQueryCriteria> sortQueryCriteria;
    private final PaginationQueryCriteria paginationQueryCriteria;


    public QueryCriteriaCollection(List<FilterQueryCriteria<?>> filterQueryCriteria, List<SortQueryCriteria> sortQueryCriteria, PaginationQueryCriteria paginationQueryCriteria) {
        this.filterQueryCriteria = filterQueryCriteria;
        this.sortQueryCriteria = sortQueryCriteria;
        this.paginationQueryCriteria = paginationQueryCriteria;
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
