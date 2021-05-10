package com.eszop.ordersservice.orders.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Order {

    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long offerId;
    private Long tierId;
    private String description;
    private OrderState state;
    private LocalDateTime creationDate;

    public Order() {
    }

    public Order(Long id, Long buyerId, Long sellerId, Long offerId, Long tierId, String description, OrderState state, LocalDateTime creationDate) {
        this.id = id;
        this.buyerId = buyerId;
        this.offerId = offerId;
        this.sellerId = sellerId;
        this.tierId = tierId;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
    }

    public Order(Long buyerId, Long offerId, Long tierId, String description) {
        this.buyerId = buyerId;
        this.offerId = offerId;
        this.tierId = tierId;
        this.description = description;
    }

    public boolean areSellerAndBuyerIdsValid(){
        List<Predicate<Order>> validationPredicates = List.of(
                (order -> !Objects.equals(sellerId, buyerId)) // sellerId and buyerId have to be different
        );

        return validationPredicates.stream().allMatch(predicate -> predicate.test(this));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Order setSellerId(Long sellerId) {
        this.sellerId = sellerId;
        return this;
    }
}
