package com.saeeed.devejump.project.tailoring.cash.model

import com.google.gson.Gson
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.network.model.UserComments

class UserDataEntityMapper :DomainMapper<UserDataEntity,UserData> {
    override fun mapToDomainModel(model: UserDataEntity): UserData {
        return UserData(
            id = model.id,
            likes =convertStringToList( model.liKes),
            bookMarks =convertStringToList( model.bookMarks),
            comments =convertStringToDataClass(model.comments)

            )
    }

    override fun mapFromDomainModel(domainModel: UserData): UserDataEntity {
        return UserDataEntity(
            id = domainModel.id,
            liKes =convertListToString( domainModel.likes),
            bookMarks =convertListToString( domainModel.bookMarks),
            comments =convertDataClassToString(domainModel.comments)//convertMapToString( domainModel.comments),

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

    fun convertStringToDataClass(comments:String):MutableList<UserComments>{
        val list: ArrayList<UserComments> = ArrayList()
        comments.let {
            for(item in it.split("},")){
                var gson = Gson()
                var result = gson.fromJson(item, UserComments::class.java)
                list.add(result)
            }

        }

        return list
    }

    fun convertDataClassToString(userComments: List<UserComments>): String {
        var gson = Gson()
        var jsonString= StringBuilder()
        for(item in userComments){
            val convertedString=gson.toJson(item)
            jsonString.append("$convertedString,")

        }

        return jsonString.toString()
    }
}