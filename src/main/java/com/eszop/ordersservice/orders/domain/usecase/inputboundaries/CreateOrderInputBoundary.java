package com.eszop.ordersservice.orders.domain.usecase.inputboundaries;

import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;

import static java.text.MessageFormat.format;

public interface CreateOrderInputBoundary {

    void create(OrderDto orderDto, OfferDto offerDto);

    class SelectedOfferDoesNotExistException extends OrdersServiceException {
        public SelectedOfferDoesNotExistException(Long offerId) {
            super(format("Offer with id({0}) does not exist", offerId));
        }
    }

    class TierDoesNotExistForSelectedOfferException extends OrdersServiceException {
        public TierDoesNotExistForSelectedOfferException(Long offerId, Long tierId) {
            super(format("Tier with id({0}) does not exist for offer with id({1})", tierId, offerId));
        }
    }
}
