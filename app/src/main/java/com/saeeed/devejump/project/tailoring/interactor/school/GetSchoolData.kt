package com.saeeed.devejump.project.tailoring.interactor.school

import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Article
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.ArticleDtoMapper
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSchoolData(
    private val retrofitService: RetrofitService,
    private val bannerMapper: BannerMapper,
    private val dtoMapper:ArticleDtoMapper
) {
    fun getBanner(
        token: String,
        query: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<Banner>>> = flow {
        try {
            emit(DataState.loading())

            //TODO fix network problem

            //   if (isNetworkAvailable) {
            val banners = getBannersFromNetwork(
                token,
                query
            )

            emit(DataState.success(banners))
            //  Log.d(TAG, "flow : ${banners.size}")

            //  }




        } catch (e: Exception) {
            emit(DataState.error<List<Banner>>(e.message ?: "Unknown Error"))
            //  Log.e(TAG, "flow error : ${e.message}")
        }
    }
    fun getArticles(
        token: String,
        isNetworkAvailable: Boolean
    ):Flow<DataState<List<Article>>> = flow {
        emit(DataState.loading())

        try {
           val networkData= retrofitService.getArticles()
            emit(DataState.success(dtoMapper.toDomainList(networkData)))

        }catch (e:Exception){
            emit(DataState.error(e.message?:"خطای نا شناخته"))

        }



    }


    private suspend fun getBannersFromNetwork(token: String, query: String): List<Banner> {
        return bannerMapper.toDomainList(
            retrofitService.getBanners(query)
        )

    }
}