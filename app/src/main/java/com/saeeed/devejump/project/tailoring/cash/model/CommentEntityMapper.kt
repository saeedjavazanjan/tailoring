package com.saeeed.devejump.project.tailoring.cash.model

import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class CommentEntityMapper : DomainMapper<CommentEntity, Comment> {


    override fun mapToDomainModel(model: CommentEntity): Comment {
        return Comment(
            id = model.id,
            comment = model.comment,
            avatar = model.avatar,
            userName = model.userName,
            userId = model.userId,
            date = model.date,
            postId = model.postId
        )
    }


    override fun mapFromDomainModel(domainModel: Comment): CommentEntity {
        return CommentEntity(
            id = domainModel.id,
            comment = domainModel.comment,
            avatar = domainModel.avatar,
            userName = domainModel.userName,
            userId = domainModel.userId,
            date = domainModel.date,
            postId = domainModel.postId
        )
    }

    fun fromEntityList(initial: List<CommentEntity>): List<Comment>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Comment>): List<CommentEntity>{
        return initial.map { mapFromDomainModel(it) }
    }
}