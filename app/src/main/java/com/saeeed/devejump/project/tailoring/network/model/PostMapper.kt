package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class PostMapper : DomainMapper<PostDto, Post> {

    override fun mapToDomainModel(model: PostDto): Post {
        return Post(
            id = model.pk,
            title = model.title,
            postType=model.postType,
            featuredImage = model.featuredImage,
            like = model.like,
            publisher = model.publisher,
            authorId=model.authorId,
            videoUrl = model.videoUrl,
            description = model.description,
            dateAdded =DateUtils.longToDate(model.longDateAdded),
            haveProduct = model.haveProduct,
        )
    }

    override fun mapFromDomainModel(domainModel: Post): PostDto {
        return PostDto(
            pk = domainModel.id,
            title = domainModel.title,
            postType=domainModel.postType,
            featuredImage = domainModel.featuredImage,
            like = domainModel.like,
            publisher = domainModel.publisher,
            authorId=domainModel.authorId,
            videoUrl = domainModel.videoUrl,
            description = domainModel.description,
            longDateAdded =  DateUtils.dateToLong(domainModel.dateAdded),
            haveProduct = domainModel.haveProduct,
        )
    }

    fun toDomainList(initial: List<PostDto>): List<Post>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Post>): List<PostDto>{
        return initial.map { mapFromDomainModel(it) }
    }



}