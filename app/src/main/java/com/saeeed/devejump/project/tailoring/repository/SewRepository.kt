package com.saeeed.devejump.project.tailoring.repository

import com.saeeed.devejump.project.tailoring.domain.model.Post

interface SewRepository {

    suspend fun search(token: String, page: Int, query: String): List<Post>

 //   suspend fun get(token: String, id: Int): Post

}