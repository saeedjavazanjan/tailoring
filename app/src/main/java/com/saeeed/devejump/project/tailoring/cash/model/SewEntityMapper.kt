package com.saeeed.devejump.project.tailoring.cash.model

import com.google.gson.Gson
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class SewEntityMapper : DomainMapper<SewEntity, SewMethod> {

    override fun mapToDomainModel(model: SewEntity): SewMethod {
        return SewMethod(
            id = model.id,
            title = model.title,
            postType=model.postType,
            featuredImage = model.featuredImage,
            like = model.like,
            publisher = model.publisher,
            videoUrl = model.video,
            description = model.description,
            comments=convertCommentsStringToList(model.comments),
            dateAdded =DateUtils.longToDate(model.dateAdded),
            dateUpdated = DateUtils.longToDate(model.dateUpdated),
        )
    }


    override fun mapFromDomainModel(domainModel: SewMethod): SewEntity {
        return SewEntity(
            id = domainModel.id,
            title = domainModel.title,
            postType=domainModel.postType,
            featuredImage = domainModel.featuredImage,
            like = domainModel.like,
            publisher = domainModel.publisher,
            video = domainModel.videoUrl,
            description = domainModel.description,
            comments=convertCommentsListToString(domainModel.comments),
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }




     fun convertCommentsListToString(comments: List<Comment>): String {
        var gson = Gson()
        var jsonString= StringBuilder()
        for(item in comments){
            val convertedString=gson.toJson(item)
            jsonString.append("$convertedString,")

        }
        return jsonString.toString()
    }

     fun convertCommentsStringToList(comments: String): List<Comment>{
        val list: ArrayList<Comment> = ArrayList()
        comments.let {
            for(item in comments.split("},")){
                var gson = Gson()
                var comment= "$item}"
                if(!item.equals("")) {
                    var result = gson.fromJson(comment, Comment::class.java)
                    list.add(result)
                }
            }

        }

        return list
    }

    fun fromEntityList(initial: List<SewEntity>): List<SewMethod>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<SewMethod>): List<SewEntity>{
        return initial.map { mapFromDomainModel(it) }
    }
}