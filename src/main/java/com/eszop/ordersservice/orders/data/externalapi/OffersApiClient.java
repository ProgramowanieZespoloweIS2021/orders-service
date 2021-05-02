package com.eszop.ordersservice.orders.data.externalapi;


import com.eszop.ordersservice.config.OffersConfig;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.OrderDto;
import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OffersApiClient {

    private final WebClient webClient;

    public OffersApiClient(OffersConfig offersConfig) {
        this.webClient = WebClient.builder().baseUrl(offersConfig.getUrl().toString()).build();
    }

    public Mono<OfferDto> getOfferMono(Long offerId) {
        return webClient.get().uri("/offers/" + offerId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new OrdersServiceException(MessageFormat.format("Offer with id {0} does not exist", offerId))))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new OrdersServiceException("Offers service encountered an error")))
                .bodyToMono(
                        OfferDto.class
                ).doOnError((error) -> {
                    if (error instanceof OrdersServiceException ordersError)
                        throw ordersError;
                    else
                        throw new OrdersServiceException("Could not get offer details");
                })
                .timeout(
                        Duration.ofSeconds(3)
                );
    }

    public OfferDto getOffer(Long offerId) {
        return getOfferMono(offerId).block();
    }

    public Map<OrderDto, OfferDto> getOfferByOrder(List<OrderDto> orders) {
        return orders.stream().map(
                order -> new AbstractMap.SimpleEntry<>(order, getOfferMono(order.offerId))
        ).map(
                offerByOrder -> new AbstractMap.SimpleEntry<>(offerByOrder.getKey(), offerByOrder.getValue().block())
        ).collect(
                Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)
        );
    }

}
