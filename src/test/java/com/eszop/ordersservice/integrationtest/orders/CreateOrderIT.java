package com.eszop.ordersservice.integrationtest.orders;

import com.eszop.ordersservice.OrdersServiceApplication;
import com.eszop.ordersservice.orders.data.externalapi.OffersApiClient;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.TierDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrdersServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Transactional
public class CreateOrderIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private OffersApiClient offersApiClient;

    private MockMvc mockMvc;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void Can_add_order() throws Exception {
        Mockito.when(offersApiClient.getOfferByOrder(Mockito.any(List.class))).thenCallRealMethod();
        Mockito.when(offersApiClient.getOfferMono(1L)).thenReturn(Mono.just(new OfferDto(1L, 1L, List.of(new TierDto(1L)))));
        var orderDtoListJson = """
                [
                     {
                         "buyerId": 1,
                         "offerId": 1,
                         "tierId": 1,
                         "description": "description"
                     }
                ]
                """;

        mockMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderDtoListJson)
        ).andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(
                get("/orders")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.[0].buyerId").value(1L)
        ).andExpect(
                jsonPath("$.[0].offerId").value(1L)
        ).andExpect(
                jsonPath("$.[0].tierId").value(1L)
        ).andExpect(
                jsonPath("$.[0].description").value("description")
        ).andExpect(
                jsonPath("$.[0].state").value("ORDERED")
        );
    }

}
