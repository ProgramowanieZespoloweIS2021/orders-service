package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.entity.OrderState;
import com.eszop.ordersservice.orders.mapper.RestApiQueryCriteriaMapper;
import com.eszop.ordersservice.querycriteria.FilterCriteria;
import com.eszop.ordersservice.querycriteria.FilterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class RestApiQueryCriteriaMapperTest {

    private RestApiQueryCriteriaMapper sut;
    private String dateTimeFormat = "yyyy-MM-dd-HH-mm-ss";
    private String dateFormat = "yyyy-MM-dd";

    @BeforeEach
    public void setUp(){
        this.sut = new RestApiQueryCriteriaMapper(dateTimeFormat, dateFormat);
    }


    @Test
    public void can_properly_map_id_filter_criteria(){
        var idToBeFilteredByFieldNames = new TreeMap<String, Object>() {{
            put("buyerId", 1L);
            put("offerId", null);
            put("tierId", null);
            put("state", OrderState.ORDERED);
        }};

        var result = sut.queryCriteriaCollectionOf(idToBeFilteredByFieldNames, Collections.emptyMap(), Collections.emptyList(), 1, 1);

        assertThat(result.getFilterQueryCriteria()).containsExactly(new FilterCriteria<>(1L, FilterType.EQUAL, "buyerId"), new FilterCriteria<>(OrderState.ORDERED, FilterType.EQUAL, "state"));
    }

    @Test
    public void can_properly_map_description_filter_criteria() throws ParseException {
        var descriptionFilterCriteria = Map.of(
          "creationDate", List.of("lt:2020-10-10", "gt:2019-10-10", "eq:2018-10-10", "ge:2020-09-10-10-10-10", "le:2020-10-01")
        );

        var result = sut.queryCriteriaCollectionOf(Collections.emptyMap(), descriptionFilterCriteria, Collections.emptyList(), 1, 1);

        assertThat(result.getFilterQueryCriteria()).containsExactly(
                new FilterCriteria<>(LocalDateTime.of(2020, 10, 10, 0, 0, 0), FilterType.LESS, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2019, 10, 10, 0, 0, 0), FilterType.GREATER, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2018, 10, 10, 0, 0, 0), FilterType.EQUAL, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2020, 9, 10, 10, 10, 10), FilterType.GREATER_EQUAL, "creationDate"),
                new FilterCriteria<>(LocalDateTime.of(2020, 10, 1, 0, 0, 0), FilterType.LESS_EQUAL, "creationDate")
        );
    }



}
