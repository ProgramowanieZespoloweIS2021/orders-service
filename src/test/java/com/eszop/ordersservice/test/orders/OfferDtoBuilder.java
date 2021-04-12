package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.dto.request.TierDto;

import java.util.Set;

public class OfferDtoBuilder {

    private Long id = 1L;
    private Long sellerId = 1L;
    private Set<TierDto> tiers = Set.of(new TierDto(1L), new TierDto(2L), new TierDto(3L));

    public OfferDtoBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public OfferDtoBuilder setTiers(Set<TierDto> tiers) {
        this.tiers = tiers;
        return this;
    }

    public OfferDtoBuilder setSellerId(Long sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    public OfferDto build(){
        return new OfferDto(this.id, this.sellerId, this.tiers);
    }

    public OfferDtoBuilder setSellerId(long sellerId) {
        this.sellerId = sellerId;
        return this;
    }
}
