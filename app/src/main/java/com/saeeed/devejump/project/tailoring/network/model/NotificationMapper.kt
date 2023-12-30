package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.Notification
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class NotificationMapper:DomainMapper<NotificationDto,Notification> {
    override fun mapToDomainModel(model: NotificationDto): Notification {
        return Notification(
            id = model.id,
            date = model.date,
            title = model.title,
            text = model.text
        )
    }

    override fun mapFromDomainModel(domainModel: Notification): NotificationDto {
        return NotificationDto(
            id = domainModel.id,
            date = domainModel.date,
            title = domainModel.title,
            text = domainModel.text
        )
    }

    fun toDomainList(initial: List<NotificationDto>): List<Notification>{
        return initial.map { mapToDomainModel(it) }
    }
}