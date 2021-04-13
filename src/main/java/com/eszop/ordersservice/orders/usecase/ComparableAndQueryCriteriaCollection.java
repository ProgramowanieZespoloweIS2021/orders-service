package com.eszop.ordersservice.orders.usecase;

import com.eszop.ordersservice.querycriteria.FilterQueryCriteria;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ComparableAndQueryCriteriaCollection extends QueryCriteriaCollection {

    private List<FilterQueryCriteria<? extends Comparable<?>>> filterQueryCriteriaOfComparable = Collections.emptyList();
    private List<FilterQueryCriteria<? extends Collection<?>>> filterQueryCriteriaOfCollection = Collections.emptyList();

    public ComparableAndQueryCriteriaCollection setFilterQueryCriteriaOfComparable(List<FilterQueryCriteria<? extends Comparable<?>>> filterQueryCriteriaofComparable) {
        this.filterQueryCriteriaOfComparable = filterQueryCriteriaofComparable;
        return this;
    }

    public ComparableAndQueryCriteriaCollection setFilterQueryCriteriaOfCollection(List<FilterQueryCriteria<? extends Collection<?>>> filterQueryCriteriaOfCollection) {
        this.filterQueryCriteriaOfCollection = filterQueryCriteriaOfCollection;
        return this;
    }

    public List<FilterQueryCriteria<? extends Comparable<?>>> getFilterQueryCriteriaOfComparable() {
        return filterQueryCriteriaOfComparable;
    }

    public List<FilterQueryCriteria<? extends Collection<?>>> getFilterQueryCriteriaOfCollection() {
        return filterQueryCriteriaOfCollection;
    }

}
