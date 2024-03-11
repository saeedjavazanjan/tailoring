package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.OnUploadPost
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class PostMapper : DomainMapper<PostDto, Post> {

    override fun mapToDomainModel(model: PostDto): Post {
        return Post(
            id = model.id!!,
            title = model.title!!,
            category=model.category!!,
            postType=model.postType!!,
            featuredImage = model.featuredImages!!,
            like = model.like!!,
            publisher = model.author!!,
            authorId=model.authorId!!,
            authorAvatar=model.authorAvatar!!,
            videoUrl = model.video!!,
            description = model.description!!,
            dateAdded =model.dataAdded!!,
            longDataAdded=model.longDataAdded!!,
            haveProduct = model.haveProduct!!,

        )
    }



    override fun mapFromDomainModel(domainModel: Post): PostDto {
        return PostDto(
            id = domainModel.id,
            title = domainModel.title,
            category=domainModel.category,
            postType=domainModel.postType,
            featuredImages = domainModel.featuredImage,
            like = domainModel.like,
            author = domainModel.publisher,
            authorId=domainModel.authorId,
            authorAvatar=domainModel.authorAvatar,
            video = domainModel.videoUrl,
            description = domainModel.description,
            dataAdded=domainModel.dateAdded,
            longDataAdded = domainModel.longDataAdded,
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