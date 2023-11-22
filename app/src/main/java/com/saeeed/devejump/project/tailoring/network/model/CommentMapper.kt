package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class CommentMapper : DomainMapper<CommentDto, Comment> {

    override fun mapToDomainModel(model: CommentDto): Comment {
        return Comment(
            id=model.id,
            comment = model.comment,
            avatar = model.avatar,
            userName = model.userName,
            userId=model.userId,
            date=model.date,
            postId=model.postId
        )
    }



    override fun mapFromDomainModel(domainModel: Comment): CommentDto {
        return CommentDto(
            id=domainModel.id,
            comment = domainModel.comment,
            avatar = domainModel.avatar,
            userName = domainModel.userName,
            userId=domainModel.userId,
            date=domainModel.date,
            postId=domainModel.postId
        )
    }

    fun toDomainList(initial: List<CommentDto>): List<Comment>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Comment>): List<CommentDto>{
        return initial.map { mapFromDomainModel(it) }
    }

}