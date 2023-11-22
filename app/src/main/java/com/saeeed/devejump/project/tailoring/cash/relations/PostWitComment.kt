package com.saeeed.devejump.project.tailoring.cash.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.saeeed.devejump.project.tailoring.cash.model.CommentEntity
import com.saeeed.devejump.project.tailoring.cash.model.SewEntity

data class PostWitComment(
    @Embedded val post: SewEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val comments: List<CommentEntity>
)