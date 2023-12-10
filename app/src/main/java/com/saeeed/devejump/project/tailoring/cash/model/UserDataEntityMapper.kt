package com.saeeed.devejump.project.tailoring.cash.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.CommentOnSpecificPost
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.model.UserPublicData
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class UserDataEntityMapper :DomainMapper<UserDataEntity,UserData?> {
    override fun mapToDomainModel(model: UserDataEntity): UserData {
        return UserData(
            userId = model.userId,
            userName=model.userName,
            phoneNumber = model.phoneNumber,
            avatar = model.avatar,
            bio=model.bio,
            followers = convertStringToUserList(model.followers),
            following = convertStringToUserList(model.following),
            likes = convertStringToList(model.liKes),
            bookMarks = convertStringToList (model.bookMarks),
            comments = convertStringToCommentList(model.comments)

            )
    }

    override fun mapFromDomainModel(domainModel: UserData?): UserDataEntity {
        return UserDataEntity(
            userId = domainModel!!.userId,
            userName=domainModel.userName,
            phoneNumber = domainModel.phoneNumber,
            avatar = domainModel.avatar,
            bio=domainModel.bio,
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
        try {
            val commentsList="["+comments +"]"
            val type = object : TypeToken<List<CommentOnSpecificPost>>() {}.type
            return gson.fromJson(commentsList, type)
        } catch(e:Exception){
            e.printStackTrace()
            return mutableListOf()
        }


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
           /* val gson = Gson()
            val type = object : TypeToken<List<CommentOnSpecificPost>>() {}.type
            jsonString.append( gson.toJson(item, type))*/

        }

        return jsonString.toString()
    }
}