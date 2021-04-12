package com.eszop.ordersservice.orders.usecase.inputboundaries;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;

import static java.text.MessageFormat.format;

public interface CreateOrderInputBoundary {

    void create(PostOrderRequest postOrderRequest, OfferDto offerDto) throws TierDoesNotExistForSelectedOfferException, SelectedOfferDoesNotExistException;

    class SelectedOfferDoesNotExistException extends Exception {

        private final String message;

        public SelectedOfferDoesNotExistException(Long offerId){
            this.message = format("Offer with id({0}) does not exist", offerId);
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
    class  TierDoesNotExistForSelectedOfferException extends Exception {
        private final String message;

        public TierDoesNotExistForSelectedOfferException(Long offerId, Long tierId){
            this.message = format("Tier with id({0}) does not exist for offer with id({1})", tierId, offerId);
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
