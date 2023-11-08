package com.saeeed.devejump.project.tailoring.cash.model

import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
class UserDataEntityMapper :DomainMapper<UserDataEntity,UserData> {
    override fun mapToDomainModel(model: UserDataEntity): UserData {
        return UserData(
            id = model.id,
            likes =convertStringToList( model.liKes),
            bookMarks =convertStringToList( model.bookMarks),
            comments = convertStringToList(model.comments)

            )
    }

    override fun mapFromDomainModel(domainModel: UserData): UserDataEntity {
        return UserDataEntity(
            id = domainModel.id,
            liKes =convertListToString( domainModel.likes),
            bookMarks =convertListToString( domainModel.bookMarks),
            comments =convertListToString( domainModel.comments),

        )
    }

    private fun convertListToString(list: List<String>): String {
        val ingredientsString = StringBuilder()
        for(item in list){
            ingredientsString.append("$item,")
        }
        return ingredientsString.toString()
    }

    private fun convertStringToList(ingredientsString: String?): List<String>{
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let {
            for(item in it.split(",")){
                list.add(item)
            }
        }
        return list
    }
}