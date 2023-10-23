package com.saeeed.devejump.project.tailoring.interactor.home

import android.util.Log
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import com.saeeed.devejump.project.tailoring.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBanners(
    private val retrofitService: RetrofitService,
    private val dtoMapper: BannerMapper,
) {

    fun execute(
        token: String,
        query: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<Banner>>> = flow {
        try {
          //  emit(DataState.loading())

            //   if (isNetworkAvailable) {
                    val banners = getBannersFromNetwork(
                        token,
                        query
                    )

                    emit(DataState.success(banners))
                  //  Log.d(TAG, "flow : ${banners.size}")

             //   }




        } catch (e: Exception) {
            emit(DataState.error<List<Banner>>(e.message ?: "Unknown Error"))
          //  Log.e(TAG, "flow error : ${e.message}")
        }
    }

    private suspend fun getBannersFromNetwork(token: String, query: String): List<Banner> {
        return dtoMapper.toDomainList(
            retrofitService.getBanners(query)
        )

    }
}