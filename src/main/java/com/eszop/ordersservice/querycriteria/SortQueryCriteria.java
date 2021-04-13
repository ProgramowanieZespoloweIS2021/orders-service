package com.eszop.ordersservice.querycriteria;

public class SortQueryCriteria implements QueryCriteria {

    private final SortType sortType;
    private final String fieldName;

    public SortQueryCriteria(SortType sortType, String fieldName) {
        this.sortType = sortType;
        this.fieldName = fieldName;
    }

    @Override
    public QueryCriteriaType getQueryCriteriaType() {
        return QueryCriteriaType.SORT;
    }

    public SortType getSortType() {
        return sortType;
    }

    public String getFieldName() {
        return fieldName;
    }
}
