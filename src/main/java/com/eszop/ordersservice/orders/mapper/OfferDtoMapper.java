package com.eszop.ordersservice.orders.mapper;

import com.eszop.ordersservice.orders.dto.request.OfferDto;
import com.eszop.ordersservice.orders.entity.Offer;
import com.eszop.ordersservice.orders.entity.Tier;

import java.util.HashSet;
import java.util.Set;

public class OfferDtoMapper {

    public static Offer toOffer(OfferDto offerDto) {
        Set<Tier> tiers = new HashSet<>();
        for (var tierDto : offerDto.tiers) {
            tiers.add(new Tier(tierDto.id));
        }
        return new Offer(offerDto.id, offerDto.ownerId, tiers);
    }
}
