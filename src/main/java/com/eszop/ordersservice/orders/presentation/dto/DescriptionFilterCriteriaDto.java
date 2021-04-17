package com.eszop.ordersservice.orders.presentation.dto;

import java.util.Collections;
import java.util.List;

public class DescriptionFilterCriteriaDto {

    public List<String> creationDate;

    public DescriptionFilterCriteriaDto(List<String> creationDate) {
        this.creationDate = creationDate;
    }

    public DescriptionFilterCriteriaDto() {
        creationDate = Collections.emptyList();
    }
}
