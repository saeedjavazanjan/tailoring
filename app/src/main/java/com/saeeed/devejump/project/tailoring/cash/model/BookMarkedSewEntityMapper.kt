package com.saeeed.devejump.project.tailoring.cash.model

import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper
import com.saeeed.devejump.project.tailoring.utils.DateUtils

class BookMarkedSewEntityMapper :DomainMapper<BookMarkedSewEntity,SewMethod> {
    override fun mapToDomainModel(model: BookMarkedSewEntity): SewMethod {
        TODO("Not yet implemented")
    }

    override fun mapFromDomainModel(domainModel: SewMethod): BookMarkedSewEntity {
        return BookMarkedSewEntity(
            id = domainModel.id,
            title = domainModel.title,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = convertIngredientListToString(domainModel.ingredients),
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }

    private fun convertIngredientListToString(ingredients: List<String>): String {
        val ingredientsString = StringBuilder()
        for(ingredient in ingredients){
            ingredientsString.append("$ingredient,")
        }
        return ingredientsString.toString()
    }
}