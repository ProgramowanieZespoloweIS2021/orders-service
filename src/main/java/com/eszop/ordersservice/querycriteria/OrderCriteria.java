package com.eszop.ordersservice.querycriteria;

import java.util.Objects;

public class OrderCriteria {

    private final OrderType orderType;
    private final String fieldName;

    public OrderCriteria(OrderType orderType, String fieldName) {
        this.orderType = orderType;
        this.fieldName = fieldName;
    }

    public OrderType getSortType() {
        return orderType;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCriteria that = (OrderCriteria) o;
        return orderType == that.orderType && Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderType, fieldName);
    }
}
