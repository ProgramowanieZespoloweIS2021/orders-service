package com.eszop.ordersservice.orders.presentation.controller;

import com.eszop.ordersservice.orders.data.externalapi.OffersApiClient;
import com.eszop.ordersservice.orders.data.externalapi.UsersApiClient;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderStateDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.mapper.KnownTotalOrdersCollectionMapper;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.CreateOrderInputBoundary;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.GetOrderInputBoundary;
import com.eszop.ordersservice.orders.domain.usecase.inputboundaries.UpdateOrderStateInputBoundary;
import com.eszop.ordersservice.orders.presentation.dto.DescriptionFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.IdFilterCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.OrderingCriteriaDto;
import com.eszop.ordersservice.orders.presentation.dto.mapper.OrderRestApiQueryCriteriaMapper;
import com.eszop.ordersservice.orders.presentation.views.response.CreateOrderResponse;
import com.eszop.ordersservice.orders.presentation.views.response.GetOrderResponse;
import com.eszop.ordersservice.orders.presentation.views.response.KnownTotalGetOrdersResponse;
import com.eszop.ordersservice.orders.presentation.views.response.UpdateOrderStateResponse;
import com.eszop.ordersservice.querycriteria.PaginationCriteria;
import com.eszop.ordersservice.querycriteria.QueryCriteriaCollection;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    private final CreateOrderInputBoundary createOrder;
    private final GetOrderInputBoundary getOrder;
    private final UpdateOrderStateInputBoundary updateOrderState;
    private final OffersApiClient offersApiClient;
    private final UsersApiClient usersApiClient;
    private final OrderRestApiQueryCriteriaMapper queryCriteriaMapper;

    public OrderController(CreateOrderInputBoundary createOrder, GetOrderInputBoundary getOrder, UpdateOrderStateInputBoundary updateOrderState, OffersApiClient offersApiClient, UsersApiClient usersApiClient, OrderRestApiQueryCriteriaMapper queryCriteriaMapper) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
        this.updateOrderState = updateOrderState;
        this.offersApiClient = offersApiClient;
        this.usersApiClient = usersApiClient;
        this.queryCriteriaMapper = queryCriteriaMapper;
    }

    @PostMapping("/{id}/state")
    public UpdateOrderStateResponse updateOrderState(@PathVariable("id") Long orderId, @RequestBody OrderStateDto orderStateDto) {
        updateOrderState.updateState(orderStateDto, orderId);
        return new UpdateOrderStateResponse();
    }

    @GetMapping("/{id}")
    public GetOrderResponse getOrder(@PathVariable("id") Long orderId) {
        Order order = getOrder.byId(orderId);
        Object offer = offersApiClient.getOffer(order.getOfferId());
        Object seller = usersApiClient.getUser(order.getSellerId());
        Object buyer = usersApiClient.getUser(order.getBuyerId());
        return new GetOrderResponse(offer, buyer, seller, order.getTierId(), order);
    }

    @PostMapping
    public CreateOrderResponse postOrder(@RequestBody List<OrderDto> orderDtos) {
        usersApiClient.throwIfUsersDoNotExist(orderDtos.stream().map(orderDto -> orderDto.buyerId).collect(Collectors.toList())); //Check if user exists
        Map<OrderDto, OfferDto> offersByOrders = offersApiClient.getOfferDtoByOrderDto(orderDtos);
        usersApiClient.throwIfUsersDoNotExist(offersByOrders.values().stream().map(offerDto -> offerDto.ownerId).collect(Collectors.toList())); //Check if user exists
        createOrder.create(offersByOrders);
        return new CreateOrderResponse();
    }

    @GetMapping
    public KnownTotalGetOrdersResponse getOrders(@RequestParam(name = "offer_id") Optional<Long> offerId,
                                                 @RequestParam(name = "buyer_id") Optional<Long> buyerId,
                                                 @RequestParam(name = "seller_id") Optional<Long> sellerId,
                                                 @RequestParam(name = "tier_id") Optional<Long> tierId,
                                                 @RequestParam Optional<OrderState> state,
                                                 @RequestParam(name = "creation_date", defaultValue = "") List<String> creationDate,
                                                 @RequestParam(name = "order_by", defaultValue = "desc:creation_date") List<String> ordering,
                                                 @RequestParam(name = "limit", defaultValue = "10") Integer pageLimit,
                                                 @RequestParam(name = "offset", defaultValue = "0") Integer pageOffset) {

        var idFilterCriteria = new IdFilterCriteriaDto(offerId, buyerId, sellerId, tierId, state);
        var descriptionFilterCriteria = new DescriptionFilterCriteriaDto(creationDate);
        var orderCriteria = new OrderingCriteriaDto(ordering);
        var paginationCriteria = new PaginationCriteria(pageLimit, pageOffset);

        QueryCriteriaCollection queryCriteria = queryCriteriaMapper.queryCriteriaCollectionOf(
                idFilterCriteria,
                descriptionFilterCriteria,
                orderCriteria,
                paginationCriteria
        );

        return KnownTotalOrdersCollectionMapper.toResponse(getOrder.byQueryCriteria(queryCriteria), usersApiClient, offersApiClient);
    }

}
