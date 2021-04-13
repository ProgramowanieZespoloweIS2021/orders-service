package com.eszop.ordersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }

    @Bean
    @Profile("prod")
    public WebClient offersWebClient() {
        return WebClient.builder().baseUrl("http://offers:8080").build();
    }

    @Bean
    @Profile("dev")
    public WebClient devOffersWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8081").build();
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }

}
