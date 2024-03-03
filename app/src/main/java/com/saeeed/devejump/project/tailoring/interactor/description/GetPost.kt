package com.saeeed.devejump.project.tailoring.interactor.description

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.PostEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostDto
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import com.saeeed.devejump.project.tailoring.network.model.ProductDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response

class GetPost (
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: PostEntityMapper,
    private val retrofitService: RetrofitService,
    private val postMapper:PostMapper,
    private val productDtoMapper: ProductDtoMapper
){

    fun execute(
        postId: Int,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Post>> = flow {
            emit(DataState.loading())

            var post = getSewFromCache(postId = postId)

            if(post != null){
                emit(DataState.success(post))
            }
            else{

                if(isNetworkAvailable){
                    val networkPost = getSewFromNetwork(postId)

                    if (networkPost.isSuccessful){
                        sewMethodDao.insertSew(
                            // map domain -> entity
                            entityMapper.mapFromDomainModel(postMapper.mapToDomainModel(networkPost.body()!!))
                        )
                        post = getSewFromCache(postId = postId)

                        if(post != null){
                            emit(DataState.success(post))
                        }
                        else{
                            throw Exception("خطای نا شناخته")
                        }
                    }else{
                        try {
                            val errMsg = networkPost.errorBody()?.string()?.let {
                                JSONObject(it).getString("error") // or whatever your message is
                            } ?: run {
                                emit(DataState.error(networkPost.code().toString()))
                            }
                            emit(DataState.error(errMsg.toString()))
                        } catch (e: Exception) {
                            emit(DataState.error("خطای سرور"))


                        }
                    }


                }else{
                    emit(DataState.error("ارتباط شما با اینترنت برقرار نیست"))

                }


            }


    }


    fun removePost(
        token:String,
        postId: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<String>> = flow {
        emit(DataState.loading())

        if(isNetworkAvailable){
            val result=retrofitService.removePost(token = token,id=postId)

            if (result.isSuccessful){
                emit(DataState.success("با موفقیت حذف شد"))
            }else{

                        emit(DataState.error(result.code().toString()))

        }

        }else{
            emit(DataState.error("ارتباط شما با اینترنت برقرار نیست"))

        }


    }


    fun getProductOfCurrentPost(
        postId: Int,
        isNetworkAvailable: Boolean,
    ):Flow<DataState<Product>> = flow {
        emit(DataState.loading())

        if(isNetworkAvailable){
            val result=retrofitService.getProduct(postId)

            if (result.isSuccessful){
                emit(DataState.success(productDtoMapper.mapToDomainModel(result.body()!!)))
            }else{
                try {
                    val errMsg = result.errorBody()?.string()?.let {
                        JSONObject(it).getString("error") // or whatever your message is
                    } ?: run {
                        emit(DataState.error(result.code().toString()))
                    }
                    emit(DataState.error(errMsg.toString()))
                } catch (e: Exception) {
                    emit(DataState.error("خطای سرور"))


                }
            }
        }else {
            emit(DataState.error("ارتباط شما با اینترنت برقرار نیست"))

        }


    }


    private suspend fun getSewFromCache(postId: Int): Post? {
        return sewMethodDao.getSewById(postId)?.let { sewEntity ->
            entityMapper.mapToDomainModel(sewEntity)
        }
    }

    private suspend fun getSewFromNetwork( postId: Int): Response<PostDto> {
        return retrofitService.getPost(postId)
    }


}