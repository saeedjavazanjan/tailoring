package com.saeeed.devejump.project.tailoring.interactor.description

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.PostEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import com.saeeed.devejump.project.tailoring.network.model.ProductDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSewMethod (
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: PostEntityMapper,
    private val retrofitService: RetrofitService,
    private val postMapper:PostMapper,
    private val productDtoMapper: ProductDtoMapper
){

    fun execute(
        postId: Int,
        token: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Post>> = flow {
        try {
            emit(DataState.loading())

            // just to show loading, cache is fast
          //  delay(1000)

            var sewMethod = getSewFromCache(postId = postId)

            if(sewMethod != null){
                emit(DataState.success(sewMethod))
            }
            // if the recipe is null, it means it was not in the cache for some reason. So get from network.
            else{

            //    if(isNetworkAvailable){
                    // get recipe from network
                    val networkRecipe = getSewFromNetwork(token, postId) // dto -> domain

                    // insert into cache
                    sewMethodDao.insertSew(
                        // map domain -> entity
                        entityMapper.mapFromDomainModel(networkRecipe)
                    )
           //     }

                // get from cache
                sewMethod = getSewFromCache(postId = postId)

                // emit and finish
                if(sewMethod != null){
                    emit(DataState.success(sewMethod))
                }
                else{
                    throw Exception("Unable to get recipe from the cache.")
                }
            }

        }catch (e: Exception){
            emit(DataState.error<Post>(e.message?: "Unknown Error"))
        }
    }


    fun getProductOfCurrentPost(
        postId: Int,
        token: String,
        isNetworkAvailable: Boolean,
    ):Flow<DataState<Product>> = flow {
        emit(DataState.loading())
        try {
            val product= productDtoMapper.mapToDomainModel( retrofitService.getProduct(token, postId))
            emit(DataState(product))
        }catch (e:Exception){
            emit(DataState.error(e.message?:"خطای نا شناخته"))
        }

    }


    private suspend fun getSewFromCache(postId: Int): Post? {
        return sewMethodDao.getSewById(postId)?.let { sewEntity ->
            entityMapper.mapToDomainModel(sewEntity)
        }
    }

    private suspend fun getSewFromNetwork(token: String, postId: Int): Post {
        return postMapper.mapToDomainModel(retrofitService.get(token, postId))
    }


}