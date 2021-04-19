package com.eszop.ordersservice.test.orders;

import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.TierDto;

import java.util.List;

public class OfferDtoBuilder {

    private Long id = 1L;
    private Long sellerId = 1L;
    private List<TierDto> tiers = List.of(new TierDto(1L), new TierDto(2L), new TierDto(3L));

    public OfferDtoBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public OfferDtoBuilder setTiers(List<TierDto> tiers) {
        this.tiers = tiers;
        return this;
    }

    public OfferDtoBuilder setSellerId(Long sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    public OfferDto build() {
        return new OfferDto(this.id, this.sellerId, this.tiers);
    }

    public OfferDtoBuilder setSellerId(long sellerId) {
        this.sellerId = sellerId;
        return this;
    }
}
