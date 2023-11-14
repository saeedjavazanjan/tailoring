package com.saeeed.devejump.project.tailoring.cash.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.CommentOnSpecificPost
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.model.UserPublicData
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class UserDataEntityMapper :DomainMapper<UserDataEntity,UserData> {
    override fun mapToDomainModel(model: UserDataEntity): UserData {
        return UserData(
            userId = model.userId,
            userName=model.userName,
            phoneNumber = model.phoneNumber,
            avatar = model.avatar,
            followers = convertStringToUserList(model.followers),
            following = convertStringToUserList(model.following),
            likes = convertStringToList(model.liKes),
            bookMarks = convertStringToList (model.bookMarks),
            comments = convertStringToCommentList(model.comments)

            )
    }

    override fun mapFromDomainModel(domainModel: UserData): UserDataEntity {
        return UserDataEntity(
            userId = domainModel.userId,
            userName=domainModel.userName,
            phoneNumber = domainModel.phoneNumber,
            avatar = domainModel.avatar,
            followers = convertUserListToString(domainModel.followers),
            following = convertUserListToString(domainModel.following),
            liKes = convertListToString(domainModel.likes),
            bookMarks =convertListToString (domainModel.bookMarks),
            comments = convertCommentListToString( domainModel.comments)

        )
    }

     fun convertListToString(list: List<String>): String {
        val ingredientsString = StringBuilder()
        for(item in list){
            ingredientsString.append("$item,")
        }
        return ingredientsString.toString()
    }

     fun convertStringToList(ingredientsString: String?): MutableList<String>{
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let {
            for(item in it.split(",")){
                list.add(item)
            }
        }
        return list
    }

    fun convertStringToCommentList(comments:String):MutableList<CommentOnSpecificPost>{
        val gson = Gson()
        val type = object : TypeToken<List<CommentOnSpecificPost>>() {}.type
        return gson.fromJson(comments, type)

      /*  val listOfCommentsWithPostId: ArrayList<CommentOnSpecificPost> = ArrayList()
        var commentsOnPost=comments.substringBeforeLast("],")
        commentsOnPost.let {
                    for (i in it.split("]}")){
                        var postId =i.substringAfterLast("],")
                        var gson = Gson()
                        var comment1= "$i}"
                        if(!i.equals("")) {
                            var result1 = gson.fromJson(comment1, CommentOnSpecificPost::class.java)
                            result1.postId=postId
                            for (j in i.split("},")) {
                                var comment2 = "$j}"
                                if (!j.equals("")) {
                                    var result2 = gson.fromJson(comment2, Comment::class.java)
                                    result1.comments.add(result2)
                                }
                            }
                            listOfCommentsWithPostId.add(result1)

                        }
                    }
                }
        return listOfCommentsWithPostId*/

    }

    fun convertUserListToString(users:List<UserPublicData>):String{
        var gson = Gson()
        var jsonString= StringBuilder()
        for(item in users){
            val convertedString=gson.toJson(item)
            jsonString.append("$convertedString,")

        }

        return jsonString.toString()

    }

    fun convertStringToUserList(users:String):List<UserPublicData>{
        val list: ArrayList<UserPublicData> = ArrayList()
        users.let {
            for(item in users.split("},")){
                var gson = Gson()
                var user= "$item}"
                if(!item.equals("")) {
                    var result = gson.fromJson(user, UserPublicData::class.java)
                    list.add(result)
                }
            }

        }

        return list
    }





    fun convertCommentListToString(userComments: List<CommentOnSpecificPost>): String {
        var gson = Gson()
        var jsonString= StringBuilder()
        for(item in userComments){
            val convertedString=gson.toJson(item)
            jsonString.append("$convertedString,")

        }

        return jsonString.toString()
    }
}