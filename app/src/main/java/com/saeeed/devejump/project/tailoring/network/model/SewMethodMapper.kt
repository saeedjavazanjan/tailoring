package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class SewMethodMapper : DomainMapper<SewMethodDto, SewMethod> {

    override fun mapToDomainModel(model: SewMethodDto): SewMethod {
        return SewMethod(
            id = model.pk,
            title = model.title,
            featuredImage = model.featuredImage,
            rating = model.rating,
            publisher = model.publisher,
            sourceUrl = model.sourceUrl,
            ingredients = model.ingredients,
            dateAdded = model.dateAdded,
            dateUpdated = model.dateUpdated,
        )
    }

    override fun mapFromDomainModel(domainModel: SewMethod): SewMethodDto {
        return SewMethodDto(
            pk = domainModel.id,
            title = domainModel.title,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            dateAdded = domainModel.dateAdded,
            dateUpdated = domainModel.dateUpdated,
        )
    }

    fun toDomainList(initial: List<SewMethodDto>): List<SewMethod>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<SewMethod>): List<SewMethodDto>{
        return initial.map { mapFromDomainModel(it) }
    }



}