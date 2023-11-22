package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class SewMethodMapper : DomainMapper<SewMethodDto, SewMethod> {

    override fun mapToDomainModel(model: SewMethodDto): SewMethod {
        return SewMethod(
            id = model.pk,
            title = model.title,
            postType=model.postType,
            featuredImage = model.featuredImage,
            like = model.like,
            publisher = model.publisher,
            videoUrl = model.videoUrl,
            description = model.description,
            dateAdded =DateUtils.longToDate(model.longDateAdded),
            dateUpdated = DateUtils.longToDate(model.longDateUpdated),
        )
    }

    override fun mapFromDomainModel(domainModel: SewMethod): SewMethodDto {
        return SewMethodDto(
            pk = domainModel.id,
            title = domainModel.title,
            postType=domainModel.postType,
            featuredImage = domainModel.featuredImage,
            like = domainModel.like,
            publisher = domainModel.publisher,
            videoUrl = domainModel.videoUrl,
            description = domainModel.description,
            longDateAdded =  DateUtils.dateToLong(domainModel.dateAdded),
            longDateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
        )
    }

    fun toDomainList(initial: List<SewMethodDto>): List<SewMethod>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<SewMethod>): List<SewMethodDto>{
        return initial.map { mapFromDomainModel(it) }
    }



}