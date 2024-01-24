package com.saeeed.devejump.project.tailoring.repository

import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostMapper

class SewRepositoryImpl (
    private val recipeService: RetrofitService,
    private val mapper: PostMapper,
): SewRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Post> {
        return mapper.toDomainList(recipeService.search(
          //  token = token, page = page, query = query
            1,"",30
        ))
    }

    override suspend fun get(token: String, id: Int): Post {
        return mapper.mapToDomainModel(recipeService.get(token = token, id))
    }

}