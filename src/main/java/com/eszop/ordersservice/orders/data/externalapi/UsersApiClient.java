package com.eszop.ordersservice.orders.data.externalapi;

import com.eszop.ordersservice.config.UsersConfig;
import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.usecase.dto.UserDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UsersApiClient {

    private final UsersConfig usersConfig;

    public UsersApiClient(UsersConfig usersConfig) {
        this.usersConfig = usersConfig;
    }

    public Mono<UserDto> getUserDtoMono(Long userId) {
        return new WebClientRequestBuilder()
                .setBaseUrl(usersConfig.getUrl().toString())
                .setResourcePath("/users/" + userId)
                .setMessageOn4xx(MessageFormat.format("User with id {0} could not be found.", userId))
                .setMessageOn5xx("Users service encountered an error.")
                .setMessageOnError("Could not get user details due to an error.")
                .buildForClass(UserDto.class);
    }

    public UserDto getUser(Long userId){
        return getUserDtoMono(userId).block();
    }

    public Map<Order, UserDto> getBuyerByOrder(List<Order> orders) {
        return getUserByList(orders, Order::getBuyerId);
    }

    public Map<Order, UserDto> getSellerByOrder(List<Order> orders) {
        return getUserByList(orders, Order::getSellerId);
    }

    public void throwIfUsersDoNotExist(List<Long> userIds){
        userIds.stream().map(this::getUserDtoMono).forEach(Mono::block);
    }

    private<T> Map<T, UserDto> getUserByList(List<T> by, Function<T, Long> userIdentityFromTypeResolver){
        return by.stream().map(
                item -> new AbstractMap.SimpleEntry<>(item, getUserDtoMono(userIdentityFromTypeResolver.apply(item)))
        ).map(
                offerByOrder -> new AbstractMap.SimpleEntry<>(offerByOrder.getKey(), offerByOrder.getValue().block())
        ).collect(
                Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)
        );
    }

}
