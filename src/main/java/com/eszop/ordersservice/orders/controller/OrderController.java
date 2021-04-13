package com.eszop.ordersservice.orders.controller;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.dto.request.TierDto;
import com.eszop.ordersservice.orders.dto.response.CreateOrderResponse;
import com.eszop.ordersservice.orders.dto.response.GetOrderResponse;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.entity.OrderState;
import com.eszop.ordersservice.orders.mapper.QueryCriteriaMapper;
import com.eszop.ordersservice.orders.usecase.inputboundaries.CreateOrderInputBoundary;
import com.eszop.ordersservice.orders.usecase.inputboundaries.GetOrderInputBoundary;
import com.eszop.ordersservice.querycriteria.FilterQueryCriteria;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static java.text.MessageFormat.format;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderInputBoundary createOrder;
    private final GetOrderInputBoundary getOrder;
    private final WebClient offersWebClient;
    //private final WebClient usersWebClient;
    private final QueryCriteriaMapper queryCriteriaMapper;

    public OrderController(CreateOrderInputBoundary createOrder, GetOrderInputBoundary getOrder, WebClient offersWebClient, QueryCriteriaMapper queryCriteriaMapper) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.offersWebClient = offersWebClient;
        this.queryCriteriaMapper = queryCriteriaMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOffer(@PathVariable("id") Long orderId) {
        try {
            Order order = getOrder.byId(orderId);
            var offerMono = offersWebClient.get().uri(format("/offers/{0}", order.getOfferId())).retrieve().bodyToMono(Object.class);
            return ResponseEntity.ok(new GetOrderResponse(offerMono.block(), order.getBuyerId(), order.getTierId(), order.getId(), order.getDescription(), order.getState(), order.getCreationDate()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@RequestBody PostOrderRequest postOrderRequest) {
        try {
//            var offerDtoMono = offersWebClient.get().uri(format("/offers/{0}", postOrderRequest.offerId))
//                    .retrieve().bodyToMono(OfferDto.class).onErrorReturn(new OfferDto(null, null, Collections.emptySet())); // TODO refactor
//            createOrder.create(postOrderRequest, offerDtoMono.block());
            createOrder.create(postOrderRequest, new OfferDto(1L, 1L, Set.of(new TierDto(1L), new TierDto(2L))));
            return ResponseEntity.ok(new CreateOrderResponse());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getOffers(@RequestParam(required = false) Long buyerId,
                                       @RequestParam(required = false) Long offerId,
                                       @RequestParam(required = false) Long tierId,
                                       @RequestParam(required = false) String creationDate,
                                       @RequestParam(required = false) OrderState state,
                                       @RequestParam(name = "sort_by", defaultValue = "desc(creationDate)") List<String> orderingCriteria,
                                       @RequestParam(name = "limit", defaultValue = "10") Integer pageLimit,
                                       @RequestParam(name = "offset", defaultValue = "0") Integer pageOffset) {

        List<FilterQueryCriteria<?>> filters = new ArrayList<>();
        filters.add(queryCriteriaMapper.mapIdToFilterQueryCriteria("buyerId", buyerId));
        filters.add(queryCriteriaMapper.mapIdToFilterQueryCriteria("offerId", offerId));
        filters.add(queryCriteriaMapper.mapIdToFilterQueryCriteria("tierId", tierId));
        filters.add(queryCriteriaMapper.mapIdToFilterQueryCriteria("state", state));
        filters = filters.stream().filter(Objects::nonNull).toList();

        var queryCriteriaCollection = new QueryCriteriaCollection(
                filters,
                queryCriteriaMapper.mapToSortQueryCriteria(orderingCriteria),
                queryCriteriaMapper.mapToPaginationQueryCriteria(pageLimit, pageOffset)
        );

        try {
            List<Order> orders = getOrder.byQueryCriteria(queryCriteriaCollection);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
