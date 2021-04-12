package com.eszop.ordersservice.orders.controller;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.PostOrderRequest;
import com.eszop.ordersservice.orders.dto.response.CreateOrderResponse;
import com.eszop.ordersservice.orders.dto.response.GetOrderResponse;
import com.eszop.ordersservice.orders.entity.Order;
import com.eszop.ordersservice.orders.usecase.inputboundaries.CreateOrderInputBoundary;
import com.eszop.ordersservice.orders.usecase.inputboundaries.GetOrderInputBoundary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

import static java.text.MessageFormat.format;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderInputBoundary createOrder;
    private final GetOrderInputBoundary getOrder;
    private final WebClient offersWebClient;
    //private final WebClient usersWebClient;

    public OrderController(CreateOrderInputBoundary createOrder, GetOrderInputBoundary getOrder, WebClient offersWebClient) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.offersWebClient = offersWebClient;
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
            var offerDtoMono = offersWebClient.get().uri(format("/offers/{0}", postOrderRequest.offerId))
                    .retrieve().bodyToMono(OfferDto.class).onErrorReturn(new OfferDto(null, null, Collections.emptySet())); // TODO refactor
            createOrder.create(postOrderRequest, offerDtoMono.block());
            return ResponseEntity.ok(new CreateOrderResponse());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
