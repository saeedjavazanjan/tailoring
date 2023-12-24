package com.saeeed.devejump.project.tailoring.interactor.home

import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Bests(
    private val retrofitService: RetrofitService,
    private val dtoMapper: PostMapper,
) {
    fun execute(
        token: String,
        query: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<Post>>> = flow {
        try {
            emit(DataState.loading())

            //TODO fix network problem

            //   if (isNetworkAvailable) {
            val sewMethod = getSewMethodsFromNetwork(
                token,
                query
            )

            emit(DataState.success(sewMethod))
            //  Log.d(TAG, "flow : ${banners.size}")

            //  }




        } catch (e: Exception) {
            emit(DataState.error<List<Post>>(e.message ?: "Unknown Error"))
            //  Log.e(TAG, "flow error : ${e.message}")
        }
    }

    private suspend fun getSewMethodsFromNetwork(token: String, query: String): List<Post> {
        return dtoMapper.toDomainList(
            retrofitService.bestsOfMonth(query=query)
        )

    }
}