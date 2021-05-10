package com.eszop.ordersservice.integrationtest.orders;

import com.eszop.ordersservice.OrdersServiceApplication;
import com.eszop.ordersservice.orders.data.externalapi.OffersApiClient;
import com.eszop.ordersservice.orders.data.externalapi.UsersApiClient;
import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.TierDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.UserDto;
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
@ActiveProfiles("test")
@Transactional
public class HappyPathIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private OffersApiClient offersApiClient;

    @MockBean
    private UsersApiClient usersApiClient;

    private MockMvc mockMvc;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void Successful_create_get_filter_update() throws Exception {
        Mockito.when(offersApiClient.getOfferDtoByOrderDto(Mockito.any(List.class))).thenCallRealMethod();
        Mockito.when(offersApiClient.getOfferDtoMono(1L)).thenReturn(Mono.just(new OfferDto(1L, 1L, List.of(new TierDto(1L), new TierDto(2L)))));

        Mockito.doCallRealMethod().when(usersApiClient).throwIfUsersDoNotExist(Mockito.any(List.class));
        Mockito.when(usersApiClient.getUser(Mockito.any(Long.class))).thenCallRealMethod();
        Mockito.when(usersApiClient.getBuyerByOrder(Mockito.any(List.class))).thenCallRealMethod();
        Mockito.when(usersApiClient.getSellerByOrder(Mockito.any(List.class))).thenCallRealMethod();
        Mockito.when(usersApiClient.getUserDtoMono(Mockito.any(Long.class))).thenReturn(Mono.just(new UserDto(){{
            firstName = "firstName";
            surname = "surname";
            email = "email";
        }}));
        var orderDtoListJson = """
                [
                    {
                        "buyerId": 2,
                        "offerId": 1,
                        "tierId": 1,
                        "description": "description"
                    },
                    {
                        "buyerId": 2,
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
        ).andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString())).andExpect(status().is2xxSuccessful());

        mockMvc.perform(
                get("/orders")
        ).andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));


        mockMvc.perform(
                get("/orders")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.orders[0].selectedTierId").value(1L)
        ).andExpect(
                jsonPath("$.orders[0].description").value("description")
        ).andExpect(
                jsonPath("$.orders[0].state").value("ORDERED")
        ).andExpect(
                jsonPath("$.totalNumberOfOrders").value(2L)
        ).andExpect(
                jsonPath("$.orders[0].buyer.firstName").value("firstName")
        ).andExpect(
                jsonPath("$.orders[0].buyer.surname").value("surname")
        ).andExpect(
                jsonPath("$.orders[0].buyer.email").value("email")
        ).andExpect(
                jsonPath("$.orders[0].seller.firstName").value("firstName")
        ).andExpect(
                jsonPath("$.orders[0].seller.surname").value("surname")
        ).andExpect(
                jsonPath("$.orders[0].seller.email").value("email")
        );

        mockMvc.perform(
                get("/orders?tier_id=2")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.totalNumberOfOrders").value(0L)
        );

        mockMvc.perform(
                post("/orders/1/state")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "value": "FINISHED"
                        }
                        """)
        ).andExpect(status().is2xxSuccessful());

        mockMvc.perform(
                get("/orders/1")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.state").value("FINISHED")
        ).andExpect(
                jsonPath("$.selectedTierId").value(1L)
        ).andExpect(
                jsonPath("$.description").value("description")
        ).andExpect(
                jsonPath("$.buyer.firstName").value("firstName")
        ).andExpect(
                jsonPath("$.buyer.surname").value("surname")
        ).andExpect(
                jsonPath("$.buyer.email").value("email")
        ).andExpect(
                jsonPath("$.seller.firstName").value("firstName")
        ).andExpect(
                jsonPath("$.seller.surname").value("surname")
        ).andExpect(
                jsonPath("$.seller.email").value("email")
        );
    }

}
