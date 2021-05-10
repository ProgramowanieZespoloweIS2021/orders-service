package com.eszop.ordersservice.orders.domain.usecase.dto;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OfferDto {

    @NotNull
    public Long id;
    @NotNull
    public Long ownerId;
    @NotEmpty
    public List<TierDto> tiers;

    public OfferDto() {

    }

    public OfferDto(Long id, Long ownerId, List<TierDto> tiers) {
        this.id = id;
        this.ownerId = ownerId;
        this.tiers = tiers;
    }
}
