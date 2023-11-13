package com.saeeed.devejump.project.tailoring.repository

import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper

class SewRepositoryImpl (
    private val recipeService: RetrofitService,
    private val mapper: SewMethodMapper,
): SewRepository {

    override suspend fun search(token: String, page: Int, query: String): List<SewMethod> {
        return mapper.toDomainList(recipeService.search(
          //  token = token, page = page, query = query
        ).sewmethods)
    }

    override suspend fun get(token: String, id: Int): SewMethod {
        return mapper.mapToDomainModel(recipeService.get(token = token, id))
    }

}