package com.eszop.ordersservice.orders.controller;

import com.eszop.ordersservice.orders.apiclient.OffersApiClient;
import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.dto.response.CreateOrderResponse;
import com.eszop.ordersservice.orders.dto.response.GetOrderResponse;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.entity.OrderState;
import com.eszop.ordersservice.orders.mapper.RestApiQueryCriteriaMapper;
import com.eszop.ordersservice.orders.usecase.inputboundaries.CreateOrderInputBoundary;
import com.eszop.ordersservice.orders.usecase.inputboundaries.GetOrderInputBoundary;
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
    private final RestApiQueryCriteriaMapper queryCriteriaMapper;

    public OrderController(CreateOrderInputBoundary createOrder, GetOrderInputBoundary getOrder, OffersApiClient offersApiClient, RestApiQueryCriteriaMapper queryCriteriaMapper) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.offersApiClient = offersApiClient;
        this.queryCriteriaMapper = queryCriteriaMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOffer(@PathVariable("id") Long orderId) {
        Order order = getOrder.byId(orderId);
        Mono<?> offerMono = offersApiClient.getOffer(order.getOfferId(), Object.class).onErrorReturn(new OfferDto(null, null, Collections.emptyList()));
        return ResponseEntity.ok(new GetOrderResponse(offerMono.block(), order.getBuyerId(), order.getTierId(), order.getId(), order.getDescription(), order.getState(), order.getCreationDate()));
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@RequestBody PostOrderRequest postOrderRequest) {
        OfferDto offerDto = offersApiClient.getOffer(postOrderRequest.offerId, OfferDto.class).block();
        createOrder.create(postOrderRequest, offerDto);
        return ResponseEntity.ok(new CreateOrderResponse());
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam(required = false) Long buyerId,
                                       @RequestParam(required = false) Long offerId,
                                       @RequestParam(required = false) Long tierId,
                                       @RequestParam(defaultValue = "", name = "creationDate") List<String> creationDateFilterCriteria,
                                       @RequestParam(required = false) OrderState state,
                                       @RequestParam(name = "order_by", defaultValue = "desc:creationDate") List<String> orderingCriteria,
                                       @RequestParam(name = "limit", defaultValue = "10") Integer pageLimit,
                                       @RequestParam(name = "offset", defaultValue = "0") Integer pageOffset) {

        QueryCriteriaCollection collection = queryCriteriaMapper.queryCriteriaCollectionOf(
                new TreeMap<>() {{
                    put("buyerId", buyerId);
                    put("offerId", offerId);
                    put("tierId", tierId);
                    put("state", state);
                }},
                Map.of(
                        "creationDate", creationDateFilterCriteria
                ),
                orderingCriteria,
                pageLimit,
                pageOffset
        );

        List<Order> orders = getOrder.byQueryCriteria(collection);
        return ResponseEntity.ok(orders);
    }

}
