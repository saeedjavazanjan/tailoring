package com.saeeed.devejump.project.tailoring.interactor.description

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSewMethod (
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: SewEntityMapper,
    private val retrofitService: RetrofitService,
    private val sewMethodMapper: SewMethodMapper,
){

    fun execute(
        postId: Int,
        token: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<SewMethod>> = flow {
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
            emit(DataState.error<SewMethod>(e.message?: "Unknown Error"))
        }
    }

    private suspend fun getSewFromCache(postId: Int): SewMethod? {
        return sewMethodDao.getSewById(postId)?.let { sewEntity ->
            entityMapper.mapToDomainModel(sewEntity)
        }
    }

    private suspend fun getSewFromNetwork(token: String, recipeId: Int): SewMethod {
        return sewMethodMapper.mapToDomainModel(retrofitService.get(token, recipeId))
    }
}