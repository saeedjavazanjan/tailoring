package com.saeeed.devejump.project.tailoring.interactor.home

import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BestOfMonth(
    private val retrofitService: RetrofitService,
    private val dtoMapper: SewMethodMapper,
) {
    fun execute(
        token: String,
        query: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<SewMethod>>> = flow {
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
            emit(DataState.error<List<SewMethod>>(e.message ?: "Unknown Error"))
            //  Log.e(TAG, "flow error : ${e.message}")
        }
    }

    private suspend fun getSewMethodsFromNetwork(token: String, query: String): List<SewMethod> {
        return dtoMapper.toDomainList(
            retrofitService.bestsOfMonth(query=query)
        )

    }
}