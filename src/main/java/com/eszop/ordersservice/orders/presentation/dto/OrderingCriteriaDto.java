package com.eszop.ordersservice.orders.presentation.dto;

import java.util.Collections;
import java.util.List;

public class OrderingCriteriaDto {

    public List<String> orderingCriteria;

    public OrderingCriteriaDto(List<String> orderingCriteria) {
        this.orderingCriteria = orderingCriteria;
    }

    public OrderingCriteriaDto() {
        orderingCriteria = Collections.emptyList();
    }
}
