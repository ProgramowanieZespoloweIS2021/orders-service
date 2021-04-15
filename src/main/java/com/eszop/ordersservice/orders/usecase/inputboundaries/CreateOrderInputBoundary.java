package com.eszop.ordersservice.orders.usecase.inputboundaries;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.exception.OrdersServiceException;

import static java.text.MessageFormat.format;

public interface CreateOrderInputBoundary {

    void create(PostOrderRequest postOrderRequest, OfferDto offerDto);

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
