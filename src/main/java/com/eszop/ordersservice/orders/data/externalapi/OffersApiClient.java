package com.eszop.ordersservice.orders.data.externalapi;


import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OffersApiClient {

    private final WebClient webClient;

    public OffersApiClient(URL offersUrl) {
        this.webClient = WebClient.builder().baseUrl(offersUrl.toString()).build();
    }

    public <T> Mono<T> getOffer(Long offerId, Class<T> aClass) {
        return webClient.get().uri("/offers/" + offerId).retrieve().bodyToMono(aClass);
    }

    public Map<OrderDto, OfferDto> getOfferByOrder(List<OrderDto> orders) {
        return orders.stream().map(
                order -> new AbstractMap.SimpleEntry<>(order, getOffer(order.offerId, OfferDto.class).onErrorReturn(new OfferDto(null, null, Collections.emptyList())))
        ).map(
                offerByOrder -> new AbstractMap.SimpleEntry<>(offerByOrder.getKey(), offerByOrder.getValue().block())
        ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

}
