package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.orders.entity.OrderState;
import com.eszop.ordersservice.orders.usecase.ComparableAndQueryCriteriaCollection;
import com.eszop.ordersservice.querycriteria.*;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class RestApiQueryCriteriaMapper implements QueryCriteriaMapper {

    Map<String, FilterType> comparableFilterTypeByFilterDescription = Map.of(
            "lt", FilterType.LESS,
            "le", FilterType.LESS_EQUAL,
            "gt", FilterType.GREATER,
            "ge", FilterType.GREATER_EQUAL,
            "eq", FilterType.EQUAL
    );

    public ComparableAndQueryCriteriaCollection of(Long buyerId, Long offerId, Long tierId, List<String> creationDateFilterCriteria, OrderState state, List<String> orderingCriteria, Integer pageLimit, Integer pageOffset){
        var toReturn = new ComparableAndQueryCriteriaCollection();

        List<FilterQueryCriteria<?>> filterCriteria = new ArrayList<>();
        filterCriteria.add(mapIdToFilterQueryCriteria("buyerId", buyerId));
        filterCriteria.add(mapIdToFilterQueryCriteria("offerId", offerId));
        filterCriteria.add(mapIdToFilterQueryCriteria("tierId", tierId));
        filterCriteria.add(mapIdToFilterQueryCriteria("state", state));
        filterCriteria = filterCriteria.stream().filter(Objects::nonNull).toList();

        List<FilterQueryCriteria<? extends Comparable<?>>> comparableFilterCriteria = new ArrayList<>();
        comparableFilterCriteria.addAll(mapFilterCriteriaDescriptionToFilterQueryCriteria("creationDate", creationDateFilterCriteria));

        List<SortQueryCriteria> sortQueryCriteria = mapToSortQueryCriteria(orderingCriteria);

        PaginationQueryCriteria paginationQueryCriteria = mapToPaginationQueryCriteria(pageLimit, pageOffset);

        toReturn.setFilterQueryCriteria(filterCriteria);
        toReturn.setFilterQueryCriteriaOfComparable(comparableFilterCriteria);
        toReturn.setSortQueryCriteria(sortQueryCriteria);
        toReturn.setPaginationQueryCriteria(paginationQueryCriteria);
        return toReturn;
    }


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
    public List<FilterQueryCriteria<? extends Comparable<?>>> mapFilterCriteriaDescriptionToFilterQueryCriteria(String fieldName, Collection<String> filterCriteriaDescriptions) {
        if (filterCriteriaDescriptions.isEmpty()) return Collections.emptyList();
        List<FilterQueryCriteria<? extends Comparable<?>>> filterQueryCriteria = new ArrayList<>();
        for (String filterCriteriaDescription : filterCriteriaDescriptions) {
            filterQueryCriteria.add(parseFilterQueryCriteria(fieldName, filterCriteriaDescription));
        }
        return filterQueryCriteria;
    }

    private FilterQueryCriteria<? extends Comparable<?>> parseFilterQueryCriteria(String fieldName,String filterCriteriaDescription){
        String valueString = filterCriteriaDescription.substring(filterCriteriaDescription.indexOf(":")+1);
        String filterDescriptionString = filterCriteriaDescription.substring(0, filterCriteriaDescription.lastIndexOf(":"));
        try{
            return new FilterQueryCriteria<>(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(valueString), comparableFilterTypeByFilterDescription.get(filterDescriptionString), fieldName);
        } catch (ParseException e) { }
        try{
            return new FilterQueryCriteria<>(new SimpleDateFormat("yyyy-MM-dd").parse(valueString), comparableFilterTypeByFilterDescription.get(filterDescriptionString), fieldName);
        } catch (ParseException e) { }
        try{
            return new FilterQueryCriteria<>(Long.valueOf(valueString), comparableFilterTypeByFilterDescription.get(filterDescriptionString), fieldName);
        } catch (NumberFormatException e){ }
        throw new RuntimeException("Filter value format nor supported!");
    }

    @Override
    public List<SortQueryCriteria> mapToSortQueryCriteria(Collection<String> sortCriteriaDescriptions) {
        if (sortCriteriaDescriptions.isEmpty()) return Collections.emptyList();
        List<SortQueryCriteria> sortQueryCriteria = new ArrayList<>();
        for (String sortCriteriaDescription : sortCriteriaDescriptions) {
            SortType sortType;
            if (sortCriteriaDescription.startsWith("desc")) sortType = SortType.DESCENDING;
            else if (sortCriteriaDescription.startsWith("asc")) sortType = SortType.ASCENDING;
            else throw new RuntimeException("Sort type unknown");
            sortQueryCriteria.add(new SortQueryCriteria(sortType, sortCriteriaDescription.substring(sortCriteriaDescription.indexOf("(") + 1, sortCriteriaDescription.lastIndexOf(")"))));
        }
        return sortQueryCriteria;
    }

    @Override
    public PaginationQueryCriteria mapToPaginationQueryCriteria(int limit, int offset) {
        return new PaginationQueryCriteria(limit, offset);
    }
}
