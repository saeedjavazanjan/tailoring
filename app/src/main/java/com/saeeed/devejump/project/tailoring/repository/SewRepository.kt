package com.saeeed.devejump.project.tailoring.repository

import com.saeeed.devejump.project.tailoring.domain.model.SewMethod

interface SewRepository {

    suspend fun search(token: String, page: Int, query: String): List<SewMethod>

    suspend fun get(token: String, id: Int): SewMethod

}