package com.eszop.ordersservice.orders.domain.usecase;

import com.eszop.ordersservice.orders.domain.entity.Offer;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.domain.entity.Tier;
import com.eszop.ordersservice.orders.domain.usecase.datagateways.CreateOrderDataSourceGateway;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.mapper.OfferMapper;
import com.eszop.ordersservice.orders.domain.usecase.dto.mapper.OrderDtoMapper;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.CreateOrderInputBoundary;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CreateOrder implements CreateOrderInputBoundary {

    private final CreateOrderDataSourceGateway createOrderDataSourcegateway;

    public CreateOrder(CreateOrderDataSourceGateway createOrderDataSourcegateway) {
        this.createOrderDataSourcegateway = createOrderDataSourcegateway;
    }

    @Override
    public void create(OrderDto orderDto, OfferDto offerDto) {
        Offer offer = OfferMapper.toOffer(offerDto);

        if (!offer.isContainingTier(new Tier(orderDto.tierId))) {
            throw new TierDoesNotExistForSelectedOfferException(orderDto.offerId, orderDto.tierId);
        }

        Order order = OrderDtoMapper.toOrder(orderDto);
        order.setSellerId(offerDto.ownerId);
        order.setCreationDate(LocalDateTime.now());
        order.setState(OrderState.ORDERED);

        if (!order.areSellerAndBuyerIdsValid()){
            throw new SellerIdAndBuyerIdAreNotValidException(order);
        }

        createOrderDataSourcegateway.create(order);
    }

    @Override
    @Transactional
    public void create(Map<OrderDto, OfferDto> offersByOrders) {
        offersByOrders.forEach(this::create);
    }

}
