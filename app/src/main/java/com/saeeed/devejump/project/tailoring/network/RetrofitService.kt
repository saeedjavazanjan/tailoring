package com.saeeed.devejump.project.tailoring.network

import com.saeeed.devejump.project.tailoring.network.model.SewMethodDto
import com.saeeed.devejump.project.tailoring.network.response.SewMethodSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {

    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): SewMethodSearchResponse

    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): SewMethodDto
}