package com.eszop.ordersservice.orders.domain.usecase.datagateways;

import java.util.Collection;
import java.util.Collections;

public record KnownTotalCollection<T>(Collection<T> items, Long total) {

    public KnownTotalCollection(Collection<T> items, Long total) {
        this.items = Collections.unmodifiableCollection(items);
        this.total = total;
    }
}
