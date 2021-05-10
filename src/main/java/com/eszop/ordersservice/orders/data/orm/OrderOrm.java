package com.eszop.ordersservice.orders.data.orm;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderOrm {

    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long offerId;
    private Long tierId;
    private String description;
    private OrderState state;
    private LocalDateTime creationDate;

    public static OrderOrm from(Order order) {
        var toReturn = new OrderOrm();
        toReturn.id = order.getId();
        toReturn.buyerId = order.getBuyerId();
        toReturn.sellerId = order.getSellerId();
        toReturn.offerId = order.getOfferId();
        toReturn.description = order.getDescription();
        toReturn.state = order.getState();
        toReturn.creationDate = order.getCreationDate();
        toReturn.tierId = order.getTierId();
        return toReturn;
    }

    public Order asOrder() {
        return new Order(id, buyerId, sellerId, offerId, tierId, description, state, creationDate);
    }

    @Id
    @SequenceGenerator(name = "orders_id_sequence", sequenceName = "orders_id_sequence", allocationSize = 40)
    @GeneratedValue(generator = "orders_id_sequence")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "buyer_id")
    @NotNull
    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "offer_id")
    @NotNull
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    @Column(name = "tier_id")
    @NotNull
    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    @Column(name = "description")
    @NotEmpty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @NotNull
    public OrderState getState() {
        return state;
    }

    public void setState(OrderState orderState) {
        this.state = orderState;
    }

    @Column(name = "creation_date")
    @NotNull
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "seller_id")
    @NotNull
    public Long getSellerId() {
        return sellerId;
    }

    public OrderOrm setSellerId(Long sellerId) {
        this.sellerId = sellerId;
        return this;
    }
}
