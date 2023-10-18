package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class BannerMapper:DomainMapper<BannerDto,Banner> {
    override fun mapToDomainModel(model: BannerDto): Banner {
        return Banner(
            id=model.id,
            title = model.title,
            imageURL = model.imageURL,
            clickDestination = model.clickDestination
        )
    }

    override fun mapFromDomainModel(domainModel: Banner): BannerDto {
        return BannerDto(
            id = domainModel.id,
            title = domainModel.title,
            imageURL = domainModel.imageURL,
            clickDestination = domainModel.clickDestination
        )
    }

    fun toDomainList(initial: List<BannerDto>): List<Banner>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Banner>): List<BannerDto>{
        return initial.map { mapFromDomainModel(it) }
    }
}