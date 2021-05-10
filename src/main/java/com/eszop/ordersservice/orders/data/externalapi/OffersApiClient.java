package com.eszop.ordersservice.orders.data.externalapi;


import com.eszop.ordersservice.config.OffersConfig;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OffersApiClient {

    private final OffersConfig offersConfig;

    public OffersApiClient(OffersConfig offersConfig) {
        this.offersConfig = offersConfig;
    }

    public Mono<OfferDto> getOfferDtoMono(Long offerId) {
        return getTypedMono(offerId, OfferDto.class);
    }

    public Mono<Object> getOfferMono(Long offerId) {
        return getTypedMono(offerId, Object.class);
    }

    public OfferDto getOfferDto(Long offerId) {
        return getOfferDtoMono(offerId).block();
    }

    public Object getOffer(Long offerId) {
        return getOfferMono(offerId).block();
    }

    public Map<Order, OfferDto> getOfferDtoByOrder(List<Order> orders) {
        return getResourceByList(orders, Order::getOfferId, OffersApiClient::getOfferDtoMono);
    }

    public Map<OrderDto, OfferDto> getOfferDtoByOrderDto(List<OrderDto> orders) {
        return getResourceByList(orders, (orderDto -> orderDto.offerId), OffersApiClient::getOfferDtoMono);
    }

    public Map<Order, Object> getOfferByOrder(List<Order> orders) {
        return getResourceByList(orders, Order::getOfferId, OffersApiClient::getOfferMono);
    }

    private<T, R> Map<T, R> getResourceByList(List<T> by, Function<T, Long> identityResolver, BiFunction<OffersApiClient, Long, Mono<R>> resourceProvider){
        return by.stream().map(
                item -> new AbstractMap.SimpleEntry<>(item, resourceProvider.apply(this, identityResolver.apply(item)))
        ).map(
                offerByOrder -> new AbstractMap.SimpleEntry<>(offerByOrder.getKey(), offerByOrder.getValue().block())
        ).collect(
                Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)
        );
    }

    private<T> Mono<T> getTypedMono(Long offerId, Class<T> aClass){
        return new WebClientRequestBuilder()
                .setBaseUrl(offersConfig.getUrl().toString())
                .setResourcePath("/offers/" + offerId)
                .setMessageOn4xx(MessageFormat.format("Offer with id {0} does not exist", offerId))
                .setMessageOn5xx("Offers service encountered an error")
                .setMessageOnError("Could not get offer details")
                .buildForClass(aClass);
    }

}
