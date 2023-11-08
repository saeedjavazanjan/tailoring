package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class UserDataMapper: DomainMapper<UserDataDto, UserData> {

    override fun mapToDomainModel(model: UserDataDto): UserData {
        return UserData(
            id = model.id,
            likes = model.likes,
            bookMarks = model.bookMarks,
            comments = model.comments
        )
    }

    override fun mapFromDomainModel(domainModel: UserData): UserDataDto {
        return UserDataDto(
            id = domainModel.id,
            likes = domainModel.likes,
            bookMarks = domainModel.bookMarks,
            comments = domainModel.comments

        )
    }

    fun toDomainList(initial: List<UserDataDto>): List<UserData>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<UserData>): List<UserDataDto>{
        return initial.map { mapFromDomainModel(it) }
    }

}