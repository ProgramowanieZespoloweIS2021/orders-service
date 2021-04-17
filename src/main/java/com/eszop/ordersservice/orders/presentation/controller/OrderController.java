package com.eszop.ordersservice.orders.presentation.controller;

import com.eszop.ordersservice.orders.data.externalapi.OffersApiClient;
import com.eszop.ordersservice.orders.domain.usecase.dto.*;
import com.eszop.ordersservice.orders.domain.usecase.dto.mapper.OrderMapper;
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
import com.eszop.ordersservice.querycriteria.PaginationCriteria;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderInputBoundary createOrder;
    private final GetOrderInputBoundary getOrder;
    private final OffersApiClient offersApiClient;
    //private final UsersApiClient usersApiClient;
    private final OrderRestApiQueryCriteriaMapper queryCriteriaMapper;

    public OrderController(CreateOrderInputBoundary createOrder, GetOrderInputBoundary getOrder, OffersApiClient offersApiClient, OrderRestApiQueryCriteriaMapper queryCriteriaMapper) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.offersApiClient = offersApiClient;
        this.queryCriteriaMapper = queryCriteriaMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOffer(@PathVariable("id") Long orderId) {
        Order order = getOrder.byId(orderId);
        Mono<?> offerMono = offersApiClient.getOffer(order.getOfferId(), Object.class).onErrorReturn(new OfferDto(null, null, Collections.emptyList()));
        return ResponseEntity.ok(new GetOrderResponse(offerMono.block(), order.getBuyerId(), order.getTierId(), OrderMapper.toOrderDto(order)));
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@RequestBody OrderDto orderDto) {
        OfferDto offerDto = offersApiClient.getOffer(orderDto.offerId, OfferDto.class).block();
        createOrder.create(orderDto, offerDto);
        return ResponseEntity.ok(new CreateOrderResponse());
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam Optional<Long> offerId,
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

        List<Order> orders = getOrder.byQueryCriteria(collection);
        return ResponseEntity.ok(orders);
    }

}
