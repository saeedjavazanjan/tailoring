package com.saeeed.devejump.project.tailoring.network

import com.saeeed.devejump.project.tailoring.domain.model.OnUpdatePost
import com.saeeed.devejump.project.tailoring.domain.model.OnUpdateProduct
import com.saeeed.devejump.project.tailoring.network.model.ArticleDto
import com.saeeed.devejump.project.tailoring.network.model.BannerDto
import com.saeeed.devejump.project.tailoring.network.model.CommentDto
import com.saeeed.devejump.project.tailoring.network.model.UpdatedCommentDto
import com.saeeed.devejump.project.tailoring.network.model.NotificationDto
import com.saeeed.devejump.project.tailoring.network.model.PeoplesDto
import com.saeeed.devejump.project.tailoring.network.model.PostDto
import com.saeeed.devejump.project.tailoring.network.model.ProductDto
import com.saeeed.devejump.project.tailoring.network.model.RegisterUserDto
import com.saeeed.devejump.project.tailoring.network.model.RegisterUserPasswordDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto
import com.saeeed.devejump.project.tailoring.network.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("posts/AuthorPosts")
    suspend fun getUserPosts(
         @Header("Authorization") token: String,
        @Query("pageNumber") page: Int,
        @Query("pageSize") pagesize:Int
    ):Response< List<PostDto>>
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

    @GET("posts/onePost")
    suspend fun getPost(
        @Query("id") id: Int
    ): Response<PostDto>

    @DELETE("posts/{id}")
    suspend fun removePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>



    @GET("products/oneProduct")
    suspend fun getProduct(
        @Query("id") postId: Int
    ): Response<ProductDto>


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


    @GET("users/currentUser")
    suspend fun userData(
          @Header("Authorization") token: String,
    ):Response<UserDataDto>

    @Multipart
    @PUT("/users/updateUser")
    suspend fun updateUseData(
        @Header("Authorization") token: String?,
        @Part("UserName") userName: RequestBody,
        @Part("Bio") bio: RequestBody,
        @Part image: MultipartBody.Part,
    ): Response<Unit>

    @Multipart
    @POST("posts/uploadPost")
    suspend fun uploadPost(
        @Header("Authorization") token: String?,
        @Part("Title") title: RequestBody,
        @Part("Category") category: RequestBody,
        @Part("PostType") postType: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("LongDataAdded") longDataAdded: RequestBody,
        @Part("HaveProduct") haveProduct: RequestBody,
        @Part Video: MultipartBody.Part,
        @Part FeaturedImages: List<MultipartBody.Part>,
        ): Response<PostDto>

    @PUT("posts/update/{id}")
    suspend fun updatePost(
        @Header("Authorization") token: String?,
        @Body onUpdatePost:OnUpdatePost,
        @Path("id") postId: Int,
        ):Response<String>

    @PUT("products/update/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String?,
        @Body onUpdateProduct: OnUpdateProduct,
        @Path("id") productId: Int,
    ):Response<String>


    @Multipart
    @POST("products/uploadProduct")
    suspend fun uploadProduct(
        @Header("Authorization") token: String?,
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("TypeOfProduct") typeOfProduct: RequestBody,
        @Part("Mas") mas: RequestBody,
        @Part("Supply") supply: RequestBody,
        @Part("Unit") unit: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("PostId") postId: RequestBody,
        @Part("AttachedFile") attachedFile: RequestBody,
        @Part Images: List<MultipartBody.Part>,
    ): Response<ProductDto>




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

    @POST("users/loginPasswordCheck")
    suspend fun loginPasswordCheck(
        @Body registerUserPasswordDto: RegisterUserPasswordDto
    ):Response<LoginResponse>

    @POST("users/registerPasswordCheck")
    suspend fun registerPasswordCheck(
        @Body registerUserPasswordDto: RegisterUserPasswordDto
    ):Response<LoginResponse>

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