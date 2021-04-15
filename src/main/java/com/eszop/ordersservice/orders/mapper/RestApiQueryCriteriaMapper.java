package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.orders.exception.OrdersServiceException;
import com.eszop.ordersservice.querycriteria.*;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Supplier;

@Component
public class RestApiQueryCriteriaMapper {

    private final SortCriteriaDescriptionMapper sortMapper = new SortCriteriaDescriptionMapper(":");
    private final FilterCriteriaDescriptionMapper filterMapper = new FilterCriteriaDescriptionMapper(":");
    private final StringConverter converter;


    private final Map<String, Class<?>> classByFilteringDescriptionSupportedFields = Map.of(
            "creationDate", LocalDateTime.class
    );

    public RestApiQueryCriteriaMapper(String dateTimeFormat, String dateFormat) {
        this.converter = new StringConverter(dateTimeFormat, dateFormat);
    }

    private <K, T, E extends OrdersServiceException> T getOrElseThrow(Map<K, T> getFrom, K key, Supplier<E> supplier) {
        if (!getFrom.containsKey(key)) throw supplier.get();
        return getFrom.get(key);
    }

    public QueryCriteriaCollection queryCriteriaCollectionOf(Map<String, ?> idToBeFilteredByFieldNames, Map<String, List<String>> filterCriteriaByFieldNames, List<String> orderingCriteria, Integer pageLimit, Integer pageOffset) {
        List<FilterCriteria<?>> filterCriteria = new ArrayList<>();
        for (var entry : idToBeFilteredByFieldNames.entrySet()) {
            filterCriteria.add(filterMapper.mapIdValue(entry.getKey(), entry.getValue()));
        }
        for (var entry : filterCriteriaByFieldNames.entrySet()) {
            if (classByFilteringDescriptionSupportedFields.containsKey(entry.getKey())) {
                filterCriteria.addAll(filterMapper.mapFilterCriteriaDescription(entry.getKey(), entry.getValue(), classByFilteringDescriptionSupportedFields.get(entry.getKey())));
            }else{
                throw new DescriptionBasedFilteringNotSupportedException(entry.getKey());
            }
        }
        filterCriteria = filterCriteria.stream().filter(Objects::nonNull).toList();

        List<OrderCriteria> orderCriteria = sortMapper.mapSortCriteriaDescription(orderingCriteria);

        PaginationCriteria paginationCriteria = new PaginationCriteria(pageLimit, pageOffset);

        return new QueryCriteriaCollection(filterCriteria, orderCriteria, paginationCriteria);
    }


    @FunctionalInterface
    interface Converter<T, R> {
        R convert(T value);

        class FormatNotSupportedException extends OrdersServiceException {
            public FormatNotSupportedException(String value) {
                super("Cannot find applicable converter for: " + value);
            }
        }
    }

    public static class FilterMappingNotFoundException extends OrdersServiceException {
        public FilterMappingNotFoundException(String filteringCriteriaDescription) {
            super(MessageFormat.format("Filter mapping not found for description: {0}", filteringCriteriaDescription));
        }
    }

    public static class OrderingMappingNotFoundException extends OrdersServiceException {
        public OrderingMappingNotFoundException(String filteringCriteriaDescription) {
            super(MessageFormat.format("Ordering mapping not found for description: {0}", filteringCriteriaDescription));
        }
    }
    public static class DescriptionBasedFilteringNotSupportedException extends OrdersServiceException{
        public DescriptionBasedFilteringNotSupportedException(String fieldName) {
            super(MessageFormat.format("Description based filtering not supported for field: {1}", fieldName));
        }
    }

    private static class StringConverter {

        private final Map<Class<?>, Converter<String, ?>> converterByClassType = new HashMap<>();

        public StringConverter(String dateTimeFormat, String dateFormat) {
            converterByClassType.put(Long.class, (longString) -> {
                try {
                    return Long.valueOf(longString);
                } catch (Exception e) {
                    throw new Converter.FormatNotSupportedException(longString);
                }
            });
            converterByClassType.put(LocalDateTime.class, (dateString) -> {
                try{
                    var formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
                    return LocalDateTime.parse(dateString, formatter);
                } catch (DateTimeParseException e){}
                try {
                    var formatter = DateTimeFormatter.ofPattern(dateFormat);
                    return LocalDateTime.of(LocalDate.parse(dateString, formatter), LocalTime.of(0,0,0));
                } catch (DateTimeParseException e){}
                throw new Converter.FormatNotSupportedException(dateString);
            });
        }

        public <T> T convert(String valueString, Class<T> aClass) {
            return (T) converterByClassType.get(aClass).convert(valueString);
        }

    }

    private class FilterCriteriaDescriptionMapper {

        private final Map<String, FilterType> filterTypeByDescription = Map.of(
                "lt", FilterType.LESS,
                "le", FilterType.LESS_EQUAL,
                "gt", FilterType.GREATER,
                "ge", FilterType.GREATER_EQUAL,
                "eq", FilterType.EQUAL
        );
        private final String separator;

        private FilterCriteriaDescriptionMapper(String separator) {
            this.separator = separator;
        }

        public <T> FilterCriteria<?> mapIdValue(String fieldName, T idValue) {
            if (idValue == null) return null;
            return new FilterCriteria<>(idValue, FilterType.EQUAL, fieldName);
        }

        public <T> List<FilterCriteria<?>> mapFilterCriteriaDescription(String fieldName, Collection<String> filterCriteriaDescriptions, Class<T> aClass) {
            List<FilterCriteria<?>> filterCriteria = new ArrayList<>();
            for (String filterCriteriaDescription : filterCriteriaDescriptions) {
                String filterTypeDescription = filterCriteriaDescription.split(separator)[0];
                String fieldValueString = filterCriteriaDescription.split(separator)[1];
                filterCriteria.add(new FilterCriteria<>(converter.convert(fieldValueString, aClass),
                        getOrElseThrow(filterTypeByDescription, filterTypeDescription, () -> new FilterMappingNotFoundException(filterTypeDescription)), fieldName));
            }
            return filterCriteria;
        }
    }

    private class SortCriteriaDescriptionMapper {

        private final Map<String, OrderType> orderTypeByDescription = Map.of(
                "desc", OrderType.DESCENDING,
                "asc", OrderType.ASCENDING
        );
        private final String separator;

        private SortCriteriaDescriptionMapper(String separator) {
            this.separator = separator;
        }

        public List<OrderCriteria> mapSortCriteriaDescription(Collection<String> sortCriteriaDescriptions) {
            List<OrderCriteria> orderCriteria = new ArrayList<>();
            for (String sortCriteriaDescription : sortCriteriaDescriptions) {
                String orderTypeDescription = sortCriteriaDescription.split(separator)[0];
                String fieldName = sortCriteriaDescription.split(separator)[1];
                orderCriteria.add(new OrderCriteria(getOrElseThrow(orderTypeByDescription, orderTypeDescription, () -> new OrderingMappingNotFoundException(orderTypeDescription)), fieldName));
            }
            return orderCriteria;
        }
    }

}
