package com.eszop.ordersservice.querycriteria;

public class FilterQueryCriteria<T> implements QueryCriteria {

    private final T value;
    private final FilterType filterType;
    private final String fieldName;

    public FilterQueryCriteria(T value, FilterType filterType, String fieldName) {
        this.value = value;
        this.filterType = filterType;
        this.fieldName = fieldName;
    }

    @Override
    public QueryCriteriaType getQueryCriteriaType() {
        return QueryCriteriaType.FILTER;
    }

    public T getValue() {
        return this.value;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public String getFieldName() {
        return fieldName;
    }
}
