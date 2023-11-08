package com.saeeed.devejump.project.tailoring.network

import com.saeeed.devejump.project.tailoring.network.model.BannerDto
import com.saeeed.devejump.project.tailoring.network.model.SewMethodDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto
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


    @GET("get_banners")
    suspend fun getBanners(
      //  @Header("Authorization") token: String,
        @Query("query") query: String
    ): List<BannerDto>

    @GET("bests")
    suspend fun bestsOfMonth(
      //  @Header("Authorization") token: String,
        @Query("query") query: String
    ): List<SewMethodDto>


    @GET("userData")
    suspend fun userData(
        //  @Header("Authorization") token: String,
        @Query("query") query: Int
    ):UserDataDto



}