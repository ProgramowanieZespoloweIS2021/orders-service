package com.eszop.ordersservice.orders.usecase;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.Offer;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.entity.Tier;
import com.eszop.ordersservice.orders.mapper.OfferDtoMapper;
import com.eszop.ordersservice.orders.mapper.OrderDtoMapper;
import com.eszop.ordersservice.orders.usecase.datagateways.CreateOrderDataSourceGateway;
import com.eszop.ordersservice.orders.usecase.inputboundaries.CreateOrderInputBoundary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateOrder implements CreateOrderInputBoundary {

    private final CreateOrderDataSourceGateway createOrderDataSourcegateway;

    public CreateOrder(CreateOrderDataSourceGateway createOrderDataSourcegateway) {
        this.createOrderDataSourcegateway = createOrderDataSourcegateway;
    }

    @Override
    public void create(PostOrderRequest postOrderRequest, OfferDto offerDto) {
        if (offerDto.id == null) {
            throw new SelectedOfferDoesNotExistException(postOrderRequest.offerId);
        }

        Order order = OrderDtoMapper.toOrder(postOrderRequest);
        order.setCreationDate(LocalDateTime.now());

        Offer offer = OfferDtoMapper.toOffer(offerDto);

        if (!offer.isContainingTier(new Tier(order.getTierId()))) {
            throw new TierDoesNotExistForSelectedOfferException(order.getOfferId(), order.getTierId());
        }

        createOrderDataSourcegateway.create(order);
    }

}
