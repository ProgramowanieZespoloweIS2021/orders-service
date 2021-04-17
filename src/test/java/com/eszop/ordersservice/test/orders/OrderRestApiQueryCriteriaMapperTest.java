package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.presentation.dto.DescriptionFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.IdFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.OrderingCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.mapper.OrderRestApiQueryCriteriaMapper;
import com.eszop.ordersservice.querycriteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class OrderRestApiQueryCriteriaMapperTest {

    private OrderRestApiQueryCriteriaMapper sut;
    private String dateTimeFormat = "yyyy-MM-dd-HH-mm-ss";
    private String dateFormat = "yyyy-MM-dd";

    @BeforeEach
    public void setUp() {
        this.sut = new OrderRestApiQueryCriteriaMapper(dateTimeFormat, dateFormat);
    }


    @Test
    public void can_properly_map_id_filter_criteria() {
        var idFilterCriteriaDto = new IdFilterCriteriaDto();
        idFilterCriteriaDto.buyerId = Optional.of(1L);
        idFilterCriteriaDto.state = Optional.of(OrderState.ORDERED);

        var result = sut.queryCriteriaCollectionOf(idFilterCriteriaDto, new DescriptionFilterCriteriaDto(), new OrderingCriteriaDto(), new PaginationCriteria(1, 1));

        assertThat(result.getFilterQueryCriteria()).containsExactly(
                new FilterCriteria<>(1L, FilterType.EQUAL, "buyerId"),
                new FilterCriteria<>(OrderState.ORDERED, FilterType.EQUAL, "state")
        );
    }

    @Test
    public void can_properly_map_description_filter_criteria() {
        var descriptionFilterCriteria = List.of("lt:2020-10-10", "gt:2019-10-10", "eq:2018-10-10", "ge:2020-09-10-10-10-10", "le:2020-10-01");

        var result = sut.queryCriteriaCollectionOf(
                new IdFilterCriteriaDto(),
                new DescriptionFilterCriteriaDto(descriptionFilterCriteria),
                new OrderingCriteriaDto(),
                new PaginationCriteria(1, 1)
        );

        assertThat(result.getFilterQueryCriteria()).containsExactly(
                new FilterCriteria<>(LocalDateTime.of(2020, 10, 10, 0, 0, 0), FilterType.LESS, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2019, 10, 10, 0, 0, 0), FilterType.GREATER, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2018, 10, 10, 0, 0, 0), FilterType.EQUAL, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2020, 9, 10, 10, 10, 10), FilterType.GREATER_EQUAL, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2020, 10, 1, 0, 0, 0), FilterType.LESS_EQUAL, "creationDate")
        );
    }


    @Test
    void can_properly_map_ordering_criteria() {
        var orderingCriteriaDto = new OrderingCriteriaDto(List.of(
                "desc:creation_date", "asc:tier_id", "desc:offer_id"
        ));

        var result = sut.queryCriteriaCollectionOf(new IdFilterCriteriaDto(), new DescriptionFilterCriteriaDto(), orderingCriteriaDto, new PaginationCriteria(1, 1));

        assertThat(result.getOrderingQueryCriteria()).containsExactly(
                new OrderCriteria(OrderType.DESCENDING, "creation_date"),
                new OrderCriteria(OrderType.ASCENDING, "tier_id"),
                new OrderCriteria(OrderType.DESCENDING, "offer_id")
        );
    }

    @Test
    void throws_on_wrong_data_format_for_description_based_filtering() {
        var descriptionFilterCriteria = new DescriptionFilterCriteriaDto(List.of("lt:123sadasfasd"));

        assertThatThrownBy(
                () -> sut.queryCriteriaCollectionOf(
                        new IdFilterCriteriaDto(),
                        descriptionFilterCriteria,
                        new OrderingCriteriaDto(),
                        new PaginationCriteria(1, 1))
        ).isInstanceOf(OrderRestApiQueryCriteriaMapper.FormatNotSupportedException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "nonexistent:2020-10-10",
            "lte:2020-10-10",
            "egt:2020-10-10"
    })
    void throws_on_wrong_filtering_type_for_description_based_filtering(String filteringCriteriaDescription) {
        var descriptionFilterCriteria = new DescriptionFilterCriteriaDto(List.of(filteringCriteriaDescription));

        assertThatThrownBy(
                () -> sut.queryCriteriaCollectionOf(
                        new IdFilterCriteriaDto(),
                        descriptionFilterCriteria,
                        new OrderingCriteriaDto(),
                        new PaginationCriteria(1, 1)
                )
        ).isInstanceOf(OrderRestApiQueryCriteriaMapper.FilterMappingNotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "hgasdgh:creation_date",
            "ascdd:creation_date",
            "descdd:creation_date"
    })
    void throws_on_wrong_ordering_type(String orderingCriteria) {
        var orderingFilterCriteria = new OrderingCriteriaDto(List.of(orderingCriteria));

        assertThatThrownBy(
                () -> sut.queryCriteriaCollectionOf(
                        new IdFilterCriteriaDto(),
                        new DescriptionFilterCriteriaDto(),
                        orderingFilterCriteria,
                        new PaginationCriteria(1, 1)
                )
        ).isInstanceOf(OrderRestApiQueryCriteriaMapper.OrderingMappingNotFoundException.class);
    }

}
