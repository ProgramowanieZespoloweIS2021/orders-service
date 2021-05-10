package com.eszop.ordersservice.orders.domain.usecase.inputboundaries;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;

import java.util.Map;

import static java.text.MessageFormat.format;

public interface CreateOrderInputBoundary {

    void create(OrderDto orderDto, OfferDto offerDto);

    void create(Map<OrderDto, OfferDto> offersByOrders);

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

    class SellerIdAndBuyerIdAreNotValidException extends OrdersServiceException{
        public SellerIdAndBuyerIdAreNotValidException(Order order) {
            super(format("Seller id({0}) and buyer id({1}) are invalid", order.getSellerId(), order.getBuyerId()));
        }
    }
}
