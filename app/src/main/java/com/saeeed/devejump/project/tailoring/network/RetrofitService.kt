package com.saeeed.devejump.project.tailoring.network

import com.saeeed.devejump.project.tailoring.network.model.ArticleDto
import com.saeeed.devejump.project.tailoring.network.model.BannerDto
import com.saeeed.devejump.project.tailoring.network.model.CommentDto
import com.saeeed.devejump.project.tailoring.network.model.UpdatedCommentDto
import com.saeeed.devejump.project.tailoring.network.model.NotificationDto
import com.saeeed.devejump.project.tailoring.network.model.PeoplesDto
import com.saeeed.devejump.project.tailoring.network.model.PostDto
import com.saeeed.devejump.project.tailoring.network.model.ProductDto
import com.saeeed.devejump.project.tailoring.network.model.RegisterUserDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET("posts/search")
    suspend fun search(
        // @Header("Authorization") token: String,
          @Query("pageNumber") page: Int,
          @Query("query") query: String,
          @Query("pageSize") pagesize:Int
    ): List<PostDto>

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

    @GET("bests")
    suspend fun getNotifications(
        //  @Header("Authorization") token: String,
    ): List<NotificationDto>

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

    @GET("comments/postComments")
    suspend fun onePostComments(
        //  @Header("Authorization") token: String,
          @Query("postId") query: Int
    ):Response< List<CommentDto>>

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

    @POST("comments")
    suspend fun commentOnPost(
        // @Header("Authorization") token: String,
        @Body comment: CommentDto
    ): Response<String>

    @POST("users/register")
    suspend fun registerPasswordRequest(
        @Body registerUser: RegisterUserDto
    ):Response<String>

    @POST("users/loginPasswordRequest")
    suspend fun loginPasswordRequest(
        @Body registerUser: RegisterUserDto
    ):Response<String>

    @PUT("comments/{commentId}")
    suspend fun editComment(
        // @Header("Authorization") token: String,
        @Path("commentId") commentId: Int,
        @Body updatedComment:UpdatedCommentDto
    ): Response<String>

    @DELETE("comments/{commentId}")
    suspend fun removeComment(
        // @Header("Authorization") token: String,
        @Path("commentId") commentId: Int,
    ): Response<Int>

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