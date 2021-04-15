package com.eszop.ordersservice.orders.entity;

import java.util.Set;

public class Offer {

    Long id;
    Long sellerId;
    Set<Tier> tiers;

    public Offer(Long id, Long sellerId, Set<Tier> tiers) {
        this.tiers = tiers;
        this.sellerId = sellerId;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isContainingTier(Tier tier) {
        return tiers.stream().anyMatch(tier1 -> tier1.id.equals(tier.id));
    }

}
