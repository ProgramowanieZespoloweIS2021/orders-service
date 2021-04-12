package com.eszop.ordersservice.orders.dto.request;

import java.util.Set;

public class OfferDto {

    public Long id;
    public Long ownerId;
    public Set<TierDto> tiers;

    public OfferDto(){

    }

    public OfferDto(Long id, Long ownerId, Set<TierDto> tiers){
        this.id = id;
        this.ownerId = ownerId;
        this.tiers = tiers;

    }
}
