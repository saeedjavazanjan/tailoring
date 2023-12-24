package com.saeeed.devejump.project.tailoring.cash.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.saeeed.devejump.project.tailoring.cash.model.FollowersEntity
import com.saeeed.devejump.project.tailoring.domain.model.UserData

data class FollowersByConsideredUserId(
    @Embedded val post: UserData,
    @Relation(
        parentColumn = "userid",
        entityColumn = "considered_user_id"
    )
    val followers: List<FollowersEntity>
)