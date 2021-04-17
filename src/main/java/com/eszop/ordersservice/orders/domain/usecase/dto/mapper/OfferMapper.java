package com.eszop.ordersservice.orders.domain.usecase.dto.mapper;

import com.eszop.ordersservice.orders.domain.usecase.dto.OfferDto;
import com.eszop.ordersservice.orders.domain.usecase.dto.TierDto;
import com.eszop.ordersservice.orders.domain.entity.Offer;
import com.eszop.ordersservice.orders.domain.entity.Tier;

import java.util.List;
import java.util.stream.Collectors;

public class OfferMapper {

    public static Offer toOffer(OfferDto offerDto) {
        List<Tier> tiers = offerDto.tiers.stream().map(TierMapper::toTier).collect(Collectors.toList());
        return new Offer(offerDto.id, offerDto.ownerId, tiers);
    }

    public static OfferDto toOfferDto(Offer offer){
        List<TierDto> tierDtos = offer.getTiers().stream().map(TierMapper::toTierDto).collect(Collectors.toList());
        return new OfferDto(offer.getId(), offer.getOwnerId(), tierDtos);
    }
}
