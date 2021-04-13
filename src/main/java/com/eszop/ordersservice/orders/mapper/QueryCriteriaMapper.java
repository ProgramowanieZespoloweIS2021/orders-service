package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.querycriteria.FilterQueryCriteria;
import com.eszop.ordersservice.querycriteria.PaginationQueryCriteria;
import com.eszop.ordersservice.querycriteria.SortQueryCriteria;

import java.util.Collection;
import java.util.List;

public interface QueryCriteriaMapper {

    <T> FilterQueryCriteria<?> mapIdToFilterQueryCriteria(String fieldName, T id);

    <T> FilterQueryCriteria<? extends Collection<?>> mapIdCollectionToFilterQueryCriteria(String fieldName, Collection<T> identifierCollection);

    FilterQueryCriteria<? extends Collection<?>> mapFilterCriteriaDescriptionToFilterQueryCriteria(String fieldName, Collection<String> filterCriteriaDescription);

    List<SortQueryCriteria> mapToSortQueryCriteria(Collection<String> sortCriteriaDescription);

    PaginationQueryCriteria mapToPaginationQueryCriteria(int limit, int offset);

}