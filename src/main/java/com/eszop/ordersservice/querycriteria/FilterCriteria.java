package com.eszop.ordersservice.querycriteria;

import java.util.Objects;

public class FilterCriteria<T> {

    private final T value;
    private final FilterType filterType;
    private final String fieldName;

    public FilterCriteria(T value, FilterType filterType, String fieldName) {
        this.value = value;
        this.filterType = filterType;
        this.fieldName = fieldName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterCriteria<?> that = (FilterCriteria<?>) o;
        return Objects.equals(value, that.value) && filterType == that.filterType && Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, filterType, fieldName);
    }
}
