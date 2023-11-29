package com.saeeed.devejump.project.tailoring.interactor.home

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import com.saeeed.devejump.project.tailoring.presentation.ui.search.STATE_KEY_PAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHomeData(
    private val retrofitService: RetrofitService,
    private val bannerMapper: BannerMapper,
    private val dtoMapper: SewMethodMapper
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

    fun getFollowingsPost(
        token: String,
        userId: Int,
        page:Int,
        isNetworkAvailable: Boolean
    ):Flow<DataState<List<SewMethod>>> = flow{
        try {
            emit(DataState.loading())

            val sewMethods = getSewMethodsFromNetwork(
                token = token,
                page = page,
                userId = userId,
            )
            emit(DataState.success(sewMethods))

        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }

    private suspend fun getBannersFromNetwork(token: String, query: String): List<Banner> {
        return bannerMapper.toDomainList(
            retrofitService.getBanners(query)
        )

    }

    private suspend fun getSewMethodsFromNetwork(
        token: String,
        page: Int,
        userId: Int
    ): List<SewMethod> {
        return dtoMapper.toDomainList(
            retrofitService.followingsPosts()
            //  token = token,
            // page = page,
            //  userId = userId,
        )
    }

}