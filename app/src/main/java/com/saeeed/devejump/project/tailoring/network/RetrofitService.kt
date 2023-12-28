package com.saeeed.devejump.project.tailoring.network

import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.network.model.ArticleDto
import com.saeeed.devejump.project.tailoring.network.model.BannerDto
import com.saeeed.devejump.project.tailoring.network.model.CommentDto
import com.saeeed.devejump.project.tailoring.network.model.PeoplesDto
import com.saeeed.devejump.project.tailoring.network.model.PostDto
import com.saeeed.devejump.project.tailoring.network.model.ProductDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto
import com.saeeed.devejump.project.tailoring.network.response.SewMethodSearchResponse
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

    @GET("search")
    suspend fun search(
        // @Header("Authorization") token: String,
        //  @Query("page") page: Int,
        //  @Query("query") query: String
    ): SewMethodSearchResponse


    @GET("followings-post")
    suspend fun followingsPosts(
        // @Header("Authorization") token: String,
        //  @Query("page") page: Int,
        //  @Query("userId") userId: Int
    ): List<PostDto>

    @GET("followings-post")
    suspend fun userPosts(
        // @Header("Authorization") token: String,
        //  @Query("page") page: Int,
        //  @Query("userId") userId: Int
    ): List<PostDto>

    @GET("following_state")
    suspend fun followingState(
         @Header("Authorization") token: String,
          @Query("userId") userId: Int
    ):Int

    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): PostDto

    @GET("get_product")
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Query("post_id") postId: Int
    ): ProductDto


    @GET("get_banners")
    suspend fun getBanners(
        //  @Header("Authorization") token: String,
        @Query("query") query: String
    ): List<BannerDto>

    @GET("get_articles")
    suspend fun getArticles(
        //  @Header("Authorization") token: String,
    ): List<ArticleDto>

    @GET("bests")
    suspend fun bestsOfMonth(
        //  @Header("Authorization") token: String,
        @Query("query") query: String
    ): List<PostDto>


    @GET("userData")
    suspend fun userData(
        //  @Header("Authorization") token: String,
        @Query("query") query: Int
    ): UserDataDto

    @POST("userData")
    suspend fun updateUseData(
        // @Header("Authorization") token: String,
        @Field("UserId") userId: Int,
        @Field("userDto") userDto: UserDataDto
    ): Int

   /* suspend fun addPassport(profile_picture: MultipartBody.Part?,
                            userid: String,
                            fistname:String,
                            surname:String,
                            nationality:String,
                            dof:String,
                            gender:String,
                            age:String,
                            sig:String,
                            salt:String,
                            image:Image):
            Response<PassportInsertApiClass>{
        return RetrofitInstance.api.addPassport(profile_picture, userid,fistname,surname,nationality,dof,gender,age,sig,salt)
    }*/

    @GET("comments")
    suspend fun onePostComments(
        //  @Header("Authorization") token: String,
        //  @Query("query") query: Int
    ): List<CommentDto>

    @POST("userData")
    suspend fun bookMark(
        // @Header("Authorization") token: String,
        @Field("UserId") userId: Int,
        @Field("postId") postId: Int
    ): Int

    @POST("userData")
    suspend fun likePost(
        // @Header("Authorization") token: String,
        @Field("UserId") userId: Int,
        @Field("postId") postId: Int
    ): Int

    @POST("userData")
    suspend fun commentOnPost(
        // @Header("Authorization") token: String,
        @Field("UserId") userId: Int,
        @Field("postId") postId: Int,
        @Field("comment") comment: String
    ): Int

    @POST("userData")
    suspend fun editComment(
        // @Header("Authorization") token: String,
        @Field("UserId") userId: Int,
        @Field("postId") postId: Int,
        @Field("comment") comment: String
    ): Int

    @POST("userData")
    suspend fun removeComment(
        // @Header("Authorization") token: String,
        @Field("UserId") userId: Int,
        @Field("postId") postId: Int,
        @Field("comment") comment: String
    ): Int

    @GET("followers")
    suspend fun getUserFollowers(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): List<PeoplesDto>

    @GET("followings")
    suspend fun getUserFollowings(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): List<PeoplesDto>


}