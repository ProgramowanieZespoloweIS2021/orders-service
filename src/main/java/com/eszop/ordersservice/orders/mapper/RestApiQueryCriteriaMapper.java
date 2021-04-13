package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.querycriteria.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class RestApiQueryCriteriaMapper implements QueryCriteriaMapper {

    @Override
    public <T> FilterQueryCriteria<?> mapIdToFilterQueryCriteria(String fieldName, T id) {
        if (id == null) return null;
        return new FilterQueryCriteria<>(id, FilterType.EQUAL, fieldName);
    }

    @Override
    public <T> FilterQueryCriteria<? extends Collection<?>> mapIdCollectionToFilterQueryCriteria(String fieldName, Collection<T> identifierCollection) {
        if (identifierCollection.isEmpty()) return null;
        return new FilterQueryCriteria<>(identifierCollection, FilterType.CONTAINS_ALL, fieldName);
    }

    @Override
    public FilterQueryCriteria<? extends Collection<?>> mapFilterCriteriaDescriptionToFilterQueryCriteria(String fieldName, Collection<String> filterCriteriaDescription) {
        if (filterCriteriaDescription.isEmpty()) return null;
        return null;
    }

    @Override
    public List<SortQueryCriteria> mapToSortQueryCriteria(Collection<String> sortCriteriaDescriptions) {
        if (sortCriteriaDescriptions.isEmpty()) return Collections.emptyList();
        List<SortQueryCriteria> sortQueryCriteria = new ArrayList<>();
        for (String sortCriteriaDescription : sortCriteriaDescriptions){
            SortType sortType;
            if(sortCriteriaDescription.startsWith("desc")) sortType = SortType.DESCENDING;
            else if(sortCriteriaDescription.startsWith("asc")) sortType = SortType.ASCENDING;
            else throw new RuntimeException("Sort type unknown");
            sortQueryCriteria.add(new SortQueryCriteria(sortType, sortCriteriaDescription.substring(sortCriteriaDescription.indexOf("(")+1, sortCriteriaDescription.lastIndexOf(")"))));
        }
        return sortQueryCriteria;
    }

    @Override
    public PaginationQueryCriteria mapToPaginationQueryCriteria(int limit, int offset) {
        return new PaginationQueryCriteria(limit, offset);
    }
}
