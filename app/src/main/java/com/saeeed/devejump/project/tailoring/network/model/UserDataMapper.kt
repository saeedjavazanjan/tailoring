package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class UserDataMapper: DomainMapper<UserDataDto, UserData> {

    override fun mapToDomainModel(model: UserDataDto): UserData {
        return UserData(
            userId = model.userId,
            userName=model.userName,
            phoneNumber = model.phoneNumber,
            avatar = model.avatar,
            followers = model.followers,
            following = model.following,
            likes = model.likes,
            bookMarks = model.bookMarks,
            comments = model.comments
        )
    }

    override fun mapFromDomainModel(domainModel: UserData): UserDataDto {
        return UserDataDto(
            userId = domainModel.userId,
            userName=domainModel.userName,
            phoneNumber = domainModel.phoneNumber,
            avatar = domainModel.avatar,
            followers = domainModel.followers,
            following = domainModel.following,
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