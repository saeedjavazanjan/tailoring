package com.saeeed.devejump.project.tailoring.network.model

import android.net.Uri
import androidx.core.net.toUri
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class ProductDtoMapper:DomainMapper<ProductDto, Product> {
    override fun mapToDomainModel(model: ProductDto): Product {
        return Product(
            id=model.id,
            name = model.name,
            description = model.description,
            images = convertListOfUriToListOfUrl(model.images),
            typeOfProduct = model.typeOfProduct,
            mas=model.mas,
            supply = model.supply,
            unit =model.unit,
            price = model.price,
            postId = model.postId,
            attachedFile = model.attachedFile
        )
    }

    override fun mapFromDomainModel(domainModel: Product): ProductDto {
        return ProductDto(
            id=domainModel.id,
            name = domainModel.name,
            description = domainModel.description,
            typeOfProduct = domainModel.typeOfProduct,
            mas=domainModel.mas,
            supply = domainModel.supply,
            unit =domainModel.unit,
            price = domainModel.price,
            postId = domainModel.postId,
            attachedFile = domainModel.attachedFile
        )
    }

    private fun convertListOfUriToListOfUrl(uris:List<String>):List<Uri>{
        val result= mutableListOf<Uri>()
        uris.forEach {url->
            val uri=url.toUri()
            result.add(uri)
        }
        return result
    }
}