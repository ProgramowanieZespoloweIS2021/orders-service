package com.eszop.ordersservice.orders.data.orm;

import com.eszop.ordersservice.orders.domain.entity.Order;
import com.eszop.ordersservice.orders.domain.entity.OrderState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderOrm {

    public static OrderOrm from(Order order) {
        var toReturn = new OrderOrm();
        toReturn.id = order.getId();
        toReturn.buyerId = order.getBuyerId();
        toReturn.offerId = order.getOfferId();
        toReturn.description = order.getDescription();
        toReturn.state = order.getState();
        toReturn.creationDate = order.getCreationDate();
        toReturn.tierId = order.getTierId();
        return toReturn;
    }

    public Order asOrder() {
        return new Order(id, buyerId, offerId, tierId, description, state, creationDate);
    }

    private Long id;
    private Long buyerId;
    private Long offerId;
    private Long tierId;
    private String description;
    private OrderState state;
    private LocalDateTime creationDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "buyer_id")
    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "offer_id")
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    @Column(name = "tier_id")
    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    public OrderState getState() {
        return state;
    }

    public void setState(OrderState orderState) {
        this.state = orderState;
    }

    @Column(name = "creation_date")
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
