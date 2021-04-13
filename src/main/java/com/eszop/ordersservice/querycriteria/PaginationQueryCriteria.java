package com.eszop.ordersservice.querycriteria;

public class PaginationQueryCriteria implements QueryCriteria {

    private final int limit;
    private final int offset;

    public PaginationQueryCriteria(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public QueryCriteriaType getQueryCriteriaType() {
        return QueryCriteriaType.PAGINATION;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

}
