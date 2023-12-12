package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.Peoples
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class PeoplesMapper:DomainMapper<PeoplesDto,Peoples> {
    override fun mapToDomainModel(model: PeoplesDto): Peoples {
        return Peoples(
            userId = model.userId,
            userName=model.userName,
            phoneNumber = model.phoneNumber,
            avatar = model.avatar,
            bio=model.bio,
            followers=model.followers,
            following=model.following,
            consideredUserId = model.consideredUseId
        )

    }

    override fun mapFromDomainModel(domainModel: Peoples): PeoplesDto {
        return PeoplesDto(
            userId = domainModel.userId,
            userName=domainModel.userName,
            phoneNumber = domainModel.phoneNumber,
            avatar = domainModel.avatar,
            bio=domainModel.bio,
            followers=domainModel.followers,
            following=domainModel.following,
            consideredUseId = domainModel.consideredUserId

        )
    }

    fun toDomainList(initial: List<PeoplesDto>): List<Peoples>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Peoples>): List<PeoplesDto>{
        return initial.map { mapFromDomainModel(it) }
    }

}