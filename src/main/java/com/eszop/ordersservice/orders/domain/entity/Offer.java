package com.eszop.ordersservice.orders.domain.entity;

import java.util.List;

public class Offer {

    private final Long id;
    private final Long ownerId;
    private final List<Tier> tiers;

    public Offer(Long id, Long sellerId, List<Tier> tiers) {
        this.tiers = tiers;
        this.ownerId = sellerId;
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<Tier> getTiers() {
        return tiers;
    }

    public Long getId() {
        return id;
    }

    public boolean isContainingTier(Tier tier) {
        return tiers.stream().anyMatch(tier1 -> tier1.id.equals(tier.id));
    }

}
