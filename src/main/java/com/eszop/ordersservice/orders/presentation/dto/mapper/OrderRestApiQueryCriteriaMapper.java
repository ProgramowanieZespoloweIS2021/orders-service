package com.eszop.ordersservice.orders.presentation.dto.mapper;

import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;
import com.eszop.ordersservice.orders.presentation.dto.DescriptionFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.IdFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.OrderingCriteriaDto;
import com.eszop.ordersservice.querycriteria.*;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderRestApiQueryCriteriaMapper {

    private final OrderingCriteriaDescriptionMapper orderingMapper = new OrderingCriteriaDescriptionMapper(":");
    private final FilterCriteriaDescriptionMapper filterMapper = new FilterCriteriaDescriptionMapper(":");
    private final StringConverter converter;

    public OrderRestApiQueryCriteriaMapper(String dateTimeFormat, String dateFormat) {
        this.converter = new StringConverter(dateTimeFormat, dateFormat);
    }

    public QueryCriteriaCollection queryCriteriaCollectionOf(IdFilterCriteriaDto idFilterCriteria, DescriptionFilterCriteriaDto descriptionFilterCriteria, OrderingCriteriaDto orderingCriteria, PaginationCriteria paginationCriteria) {
        List<FilterCriteria<?>> filterCriteria = new ArrayList<>();
        idFilterCriteria.buyerId.map(item -> filterCriteria.add(filterMapper.mapIdValue("buyerId", item)));
        idFilterCriteria.offerId.map(item -> filterCriteria.add(filterMapper.mapIdValue("offerId", item)));
        idFilterCriteria.state.map(item -> filterCriteria.add(filterMapper.mapIdValue("state", item)));
        idFilterCriteria.tierId.map(item -> filterCriteria.add(filterMapper.mapIdValue("tierId", item)));

        filterCriteria.addAll(filterMapper.mapFilterCriteriaDescription("creationDate", descriptionFilterCriteria.creationDate, LocalDateTime.class));

        List<OrderCriteria> orderCriteria = orderingMapper.mapOrderingCriteriaDescription(orderingCriteria.orderingCriteria);

        return new QueryCriteriaCollection(filterCriteria, orderCriteria, paginationCriteria);
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

    public static class FormatNotSupportedException extends OrdersServiceException {
        public FormatNotSupportedException(String value) {
            super("Cannot find applicable converter for: " + value);
        }
    }

    private static class StringConverter {

        private final Map<Class<?>, Converter<Optional<Object>>> converterByClassType = new HashMap<>();

        private final String dateFormat;
        private final String dateTimeFormat;

        public StringConverter(String dateTimeFormat, String dateFormat) {
            this.dateFormat = dateFormat;
            this.dateTimeFormat = dateTimeFormat;
            converterByClassType.put(Long.class, optionalConvert(Long::valueOf));
            converterByClassType.put(LocalDateTime.class, optionalConvert(this::convertToDateTime));
        }

        private static <T> Converter<Optional<T>> optionalConvert(Function<String, T> supplier) {
            return (String value) -> {
                try {
                    return Optional.of(supplier.apply(value));
                } catch (Exception e) {
                    return Optional.empty();
                }
            };
        }

        private LocalDateTime convertToDateTime(String dateString) {
            try {
                var formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
            }
            try {
                var formatter = DateTimeFormatter.ofPattern(dateFormat);
                return LocalDateTime.of(LocalDate.parse(dateString, formatter), LocalTime.of(0, 0, 0));
            } catch (DateTimeParseException e) {
            }
            throw new IllegalArgumentException();
        }

        public <T> T convert(String valueString, Class<T> aClass) {
            return (T) converterByClassType.get(aClass).convert(valueString).orElseThrow(() -> new FormatNotSupportedException(valueString));
        }

        @FunctionalInterface
        interface Converter<T> {
            T convert(String value);
        }

    }

    private <K, T> Optional<T> getOptional(Map<K, T> getFrom, K key) {
        if (!getFrom.containsKey(key)) return Optional.empty();
        return Optional.of(getFrom.get(key));
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
            return new FilterCriteria<>(idValue, FilterType.EQUAL, fieldName);
        }

        public <T> List<FilterCriteria<?>> mapFilterCriteriaDescription(String fieldName, Collection<String> filterCriteriaDescriptions, Class<T> aClass) {
            return filterCriteriaDescriptions.stream()
                    .map(filterCriteriaDescription -> this.mapSingleFilterCriteriaDescription(fieldName, aClass, filterCriteriaDescription))
                    .collect(Collectors.toList());
        }

        private <T> FilterCriteria<T> mapSingleFilterCriteriaDescription(String fieldName, Class<T> aClass, String filterCriteriaDescription) {
            String filterTypeDescription = filterCriteriaDescription.split(separator)[0];
            String fieldValueString = filterCriteriaDescription.split(separator)[1];
            return new FilterCriteria<>(converter.convert(fieldValueString, aClass), getOptional(filterTypeByDescription, filterTypeDescription).orElseThrow(() -> new FilterMappingNotFoundException(filterTypeDescription)), fieldName);
        }

    }

    private class OrderingCriteriaDescriptionMapper {

        private final Map<String, OrderType> orderTypeByDescription = Map.of(
                "desc", OrderType.DESCENDING,
                "asc", OrderType.ASCENDING
        );
        private final String separator;

        private OrderingCriteriaDescriptionMapper(String separator) {
            this.separator = separator;
        }

        public List<OrderCriteria> mapOrderingCriteriaDescription(Collection<String> orderingCriteriaDescriptions) {
            return orderingCriteriaDescriptions.stream()
                    .map(this::mapSingleOrderingCriteriaDescription)
                    .collect(Collectors.toList());
        }

        private OrderCriteria mapSingleOrderingCriteriaDescription(String orderingCriteriaDescription) {
            String orderTypeDescription = orderingCriteriaDescription.split(separator)[0];
            String fieldName = orderingCriteriaDescription.split(separator)[1];
            return new OrderCriteria(getOptional(orderTypeByDescription, orderTypeDescription).orElseThrow(() -> new OrderingMappingNotFoundException(orderTypeDescription)), fieldName);
        }

    }

}
