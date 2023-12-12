package com.saeeed.devejump.project.tailoring.cash.model

import com.saeeed.devejump.project.tailoring.domain.model.Peoples
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class FollowersEntityMapper:DomainMapper<FollowersEntity,Peoples> {
    override fun mapToDomainModel(model: FollowersEntity): Peoples {
        return Peoples(
            userId = model.userId,
            userName=model.userName,
            phoneNumber = model.phoneNumber,
            avatar = model.avatar,
            bio=model.bio,
            followers=model.followers,
            following=model.following,
           consideredUserId = model.consideredUserId

        )

    }

    override fun mapFromDomainModel(domainModel: Peoples): FollowersEntity {

        return FollowersEntity(
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

    fun fromEntityList(initial: List<FollowersEntity>): List<Peoples>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Peoples>): List<FollowersEntity>{
        return initial.map { mapFromDomainModel(it) }
    }
}