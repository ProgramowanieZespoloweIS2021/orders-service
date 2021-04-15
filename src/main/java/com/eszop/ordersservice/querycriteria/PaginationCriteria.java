package com.eszop.ordersservice.querycriteria;

import java.util.Objects;

public class PaginationCriteria {

    private final int limit;
    private final int offset;

    public PaginationCriteria(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaginationCriteria that = (PaginationCriteria) o;
        return limit == that.limit && offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset);
    }
}
