package com.saeeed.devejump.project.tailoring.cash.model

import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class PostEntityMapper : DomainMapper<PostEntity, Post> {

    override fun mapToDomainModel(model: PostEntity): Post {
        return Post(
            id = model.id,
            title = model.title,
            postType=model.postType,
            featuredImage = convertStringToList(model.featuredImage),
            like = model.like,
            publisher = model.publisher,
            authorId=model.authorId,
            videoUrl = model.video,
            description = model.description,
            dateAdded =DateUtils.longToDate(model.dateAdded),
            haveProduct = model.haveProduct,
        )
    }


    override fun mapFromDomainModel(domainModel: Post): PostEntity {
        return PostEntity(
            id = domainModel.id,
            title = domainModel.title,
            postType=domainModel.postType,
            featuredImage =convertListToString( domainModel.featuredImage),
            like = domainModel.like,
            publisher = domainModel.publisher,
            authorId=domainModel.authorId,
            video = domainModel.videoUrl,
            description = domainModel.description,
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            haveProduct = domainModel.haveProduct,
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }

    private fun convertListToString(list: List<String>): String {
        val string = StringBuilder()
        for(item in list){
            string.append("$item,")
        }
        return string.toString()
    }

    private fun convertStringToList(string: String?): MutableList<String>{
        val list: ArrayList<String> = ArrayList()
        string?.let {
            for(item in it.split(",")){
                if (!item.equals("")){
                    list.add(item)

                }
            }
        }
        return list
    }


   /*  fun convertCommentsListToString(comments: List<Comment>): String {
        var gson = Gson()
        var jsonString= StringBuilder()
        for(item in comments){
            val convertedString=gson.toJson(item)
            jsonString.append("$convertedString,")

        }
        return jsonString.toString()
    }

     fun convertCommentsStringToList(comments: String): MutableList<Comment>{
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
*/
    fun fromEntityList(initial: List<PostEntity>): List<Post>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Post>): List<PostEntity>{
        return initial.map { mapFromDomainModel(it) }
    }
}