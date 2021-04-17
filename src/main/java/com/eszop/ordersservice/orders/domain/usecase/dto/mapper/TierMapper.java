package com.eszop.ordersservice.orders.domain.usecase.dto.mapper;

import com.eszop.ordersservice.orders.domain.usecase.dto.TierDto;
import com.eszop.ordersservice.orders.domain.entity.Tier;

public class TierMapper {

    public static Tier toTier(TierDto tierDto){
        return new Tier(tierDto.id);
    }

    public static TierDto toTierDto(Tier tier){
        return new TierDto(tier.id);
    }

}
