package com.saeeed.devejump.project.tailoring.cash.model

import com.saeeed.devejump.project.tailoring.domain.model.Peoples
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class FollowingsEntityMapper:DomainMapper<FollowingEntity,Peoples> {
    override fun mapToDomainModel(model: FollowingEntity): Peoples {
        return Peoples(
            userId = model.userId,
            userName=model.userName,
            phoneNumber = model.phoneNumber,
            avatar = model.avatar,
            bio=model.bio,
            followers=model.followers,
            following=model.following,
            consideredUserId = model.consideredUserId

        )    }

    override fun mapFromDomainModel(domainModel: Peoples): FollowingEntity {
        return FollowingEntity(
            userId = domainModel!!.userId,
            userName=domainModel.userName,
            phoneNumber = domainModel.phoneNumber,
            avatar = domainModel.avatar,
            bio=domainModel.bio,
            followers=domainModel.followers,
            following=domainModel.following,
            consideredUserId = domainModel.consideredUserId
        )
    }
    fun fromEntityList(initial: List<FollowingEntity>): List<Peoples>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Peoples>): List<FollowingEntity>{
        return initial.map { mapFromDomainModel(it) }
    }
}