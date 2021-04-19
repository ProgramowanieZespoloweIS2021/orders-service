package com.eszop.ordersservice.orders.presentation.controller;

import com.eszop.ordersservice.orders.data.externalapi.OffersApiClient;
import com.eszop.ordersservice.orders.domain.usecase.dto.*;
import com.eszop.ordersservice.orders.domain.usecase.dto.mapper.OrderMapper;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.UpdateOrderStateInputBoundary;
import com.eszop.ordersservice.orders.presentation.dto.DescriptionFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.IdFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.OrderingCriteriaDto;
import com.eszop.ordersservice.orders.presentation.views.response.CreateOrderResponse;
import com.eszop.ordersservice.orders.presentation.views.response.GetOrderResponse;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.presentation.dto.mapper.OrderRestApiQueryCriteriaMapper;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.CreateOrderInputBoundary;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.GetOrderInputBoundary;
import com.eszop.ordersservice.orders.presentation.views.response.UpdateOrderStateResponse;
import com.eszop.ordersservice.querycriteria.PaginationCriteria;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderInputBoundary createOrder;
    private final GetOrderInputBoundary getOrder;
    private final UpdateOrderStateInputBoundary updateOrderState;
    private final OffersApiClient offersApiClient;
    //private final UsersApiClient usersApiClient;
    private final OrderRestApiQueryCriteriaMapper queryCriteriaMapper;

    public OrderController(CreateOrderInputBoundary createOrder, GetOrderInputBoundary getOrder, UpdateOrderStateInputBoundary updateOrderState, OffersApiClient offersApiClient, OrderRestApiQueryCriteriaMapper queryCriteriaMapper) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.updateOrderState = updateOrderState;
        this.offersApiClient = offersApiClient;
        this.queryCriteriaMapper = queryCriteriaMapper;
    }

    @PostMapping("/{id}/state")
    public UpdateOrderStateResponse updateOrderState(@PathVariable("id")Long orderId, @RequestBody OrderStateDto orderStateDto){
        updateOrderState.updateState(orderStateDto, orderId);
        return new UpdateOrderStateResponse();
    }

    @GetMapping("/{id}")
    public GetOrderResponse getOrder(@PathVariable("id") Long orderId) {
        Order order = getOrder.byId(orderId);
        Mono<?> offerMono = offersApiClient.getOffer(order.getOfferId(), Object.class).onErrorReturn(new OfferDto(null, null, Collections.emptyList()));
        return new GetOrderResponse(offerMono.block(), order.getBuyerId(), order.getTierId(), OrderMapper.toOrderDto(order));
    }

//    @PostMapping
//    public CreateOrderResponse postOrder(@RequestBody OrderDto orderDto) {
//        OfferDto offerDto = offersApiClient.getOffer(orderDto.offerId, OfferDto.class).block();
//        createOrder.create(orderDto, offerDto);
//        return new CreateOrderResponse();
//    }

    @PostMapping
    public CreateOrderResponse postOrder(@RequestBody List<OrderDto> orderDtos){
        createOrder.create(offersApiClient.getOfferByOrder(orderDtos));
        return new CreateOrderResponse();
    }

    @GetMapping
    public List<Order> getOrders(@RequestParam Optional<Long> offerId,
                                       @RequestParam Optional<Long> buyerId,
                                       @RequestParam Optional<Long> tierId,
                                       @RequestParam Optional<OrderState> state,
                                       @RequestParam(name = "creation_date", defaultValue = "") List<String> creationDate,
                                       @RequestParam(name = "order_by", defaultValue = "desc:creation_date") List<String> ordering,
                                       @RequestParam(name = "limit", defaultValue = "10") Integer pageLimit,
                                       @RequestParam(name = "offset", defaultValue = "0") Integer pageOffset) {

        var idFilterCriteria = new IdFilterCriteriaDto(offerId, buyerId, tierId, state);
        var descriptionFilterCriteria = new DescriptionFilterCriteriaDto(creationDate);
        var orderCriteria = new OrderingCriteriaDto(ordering);
        var paginationCriteria = new PaginationCriteria(pageLimit, pageOffset);

        QueryCriteriaCollection collection = queryCriteriaMapper.queryCriteriaCollectionOf(
                idFilterCriteria,
                descriptionFilterCriteria,
                orderCriteria,
                paginationCriteria
        );

        return getOrder.byQueryCriteria(collection);
    }

}
