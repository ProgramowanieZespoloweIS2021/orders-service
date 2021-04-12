package com.eszop.ordersservice.orders.usecase;

import com.eszop.ordersservice.orders.adapter.OfferDtoAdapter;
import com.eszop.ordersservice.orders.adapter.OrderDtoAdapter;
import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.entity.Offer;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.entity.Tier;
import com.eszop.ordersservice.orders.usecase.datagateways.CreateOrderDataSourceGateway;
import com.eszop.ordersservice.orders.usecase.inputboundaries.CreateOrderInputBoundary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateOrder implements CreateOrderInputBoundary {

    private final CreateOrderDataSourceGateway createOrderDataSourcegateway;

    public CreateOrder(CreateOrderDataSourceGateway createOrderDataSourcegateway) {
        this.createOrderDataSourcegateway = createOrderDataSourcegateway;
    }

    @Override
    public void create(PostOrderRequest postOrderRequest, OfferDto offerDto) throws TierDoesNotExistForSelectedOfferException, SelectedOfferDoesNotExistException {
        if(offerDto.id==null){
            throw new SelectedOfferDoesNotExistException(postOrderRequest.offerId);
        }

        Order order = OrderDtoAdapter.toOrder(postOrderRequest);
        order.setCreationDate(new Date());

        Offer offer = OfferDtoAdapter.toOffer(offerDto);

        if(!offer.isContainingTier(new Tier(order.getTierId()))){
            throw new TierDoesNotExistForSelectedOfferException(order.getOfferId(), order.getTierId());
        }

        createOrderDataSourcegateway.create(order);
    }

}
