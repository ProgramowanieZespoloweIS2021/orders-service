package com.eszop.ordersservice.orders.data.externalapi;

import com.eszop.ordersservice.orders.domain.usecase.exception.OrdersServiceException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WebClientRequestBuilder {

    private String baseUrl;
    private String resourcePath;
    private String messageOn4xx;
    private String messageOn5xx;
    private String messageOnError;

    public WebClientRequestBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public WebClientRequestBuilder setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }

    public WebClientRequestBuilder setMessageOn4xx(String messageOn4xx) {
        this.messageOn4xx = messageOn4xx;
        return this;
    }

    public WebClientRequestBuilder setMessageOn5xx(String messageOn5xx) {
        this.messageOn5xx = messageOn5xx;
        return this;
    }

    public WebClientRequestBuilder setMessageOnError(String messageOnError) {
        this.messageOnError = messageOnError;
        return this;
    }

    public <T> Mono<T> buildForClass(Class<T> aClass) {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        return webClient.get().uri(resourcePath)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new OrdersServiceException(messageOn4xx)))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new OrdersServiceException(messageOn5xx)))
                .bodyToMono(
                        aClass
                ).doOnError((error) -> {
                    if (error instanceof OrdersServiceException ordersError)
                        throw ordersError;
                    else
                        throw new OrdersServiceException(messageOnError);
                }).switchIfEmpty(
                        Mono.error(new OrdersServiceException(messageOn4xx))
                ).timeout(
                        Duration.ofSeconds(10)
                );
    }
}
