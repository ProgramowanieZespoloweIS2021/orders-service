package com.eszop.ordersservice.orders.dto.request;

import java.util.List;

public class OfferDto {

    public Long id;
    public Long ownerId;
    public List<TierDto> tiers;

    public OfferDto(){

    }

    public OfferDto(Long id, Long ownerId, List<TierDto> tiers){
        this.id = id;
        this.ownerId = ownerId;
        this.tiers = tiers;

    }
}
