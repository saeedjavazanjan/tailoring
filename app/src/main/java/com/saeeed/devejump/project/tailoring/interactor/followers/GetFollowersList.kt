package com.saeeed.devejump.project.tailoring.interactor.followers

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.FollowersEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Peoples
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PeoplesMapper
import com.saeeed.devejump.project.tailoring.utils.FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFollowersList(
    private val  followersEntityMapper: FollowersEntityMapper,
    private val   dtoMapper: PeoplesMapper,
    private val    retrofitService: RetrofitService,
    private val   sewMethodDao: SewMethodDao
) {

    fun execute(
        token: String,
        page: Int,
        query: String,
        isNetworkAvailable:Boolean
    ):Flow<DataState<List<Peoples>>> = flow{
        emit(DataState.loading())
            val remoteResult=getFollowersFromServer(token,page,query)

        sewMethodDao.insertUserFollowers(followersEntityMapper.toEntityList(remoteResult))

       val cashedResult=sewMethodDao.getAllFollowers(page)
        emit(DataState.success(followersEntityMapper.fromEntityList(cashedResult)))

    }

    fun restoreFollowers(
        page: Int,
        query: String
    ): Flow<DataState<List<Peoples>>> = flow {
        try {
            emit(DataState.loading())
            // query the cache
            val cacheResult = if (query.isBlank()){
                sewMethodDao.restoreAllFollowers(
                    pageSize = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }
            else{
                sewMethodDao.restoreFollowers(
                    query = query,
                    pageSize = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            // emit List<Recipe> from cache
            val list = followersEntityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        }catch (e: Exception){
            emit(DataState.error(e.message?: "Unknown Error"))
        }
    }

    fun checkIfUserFollowed(
        userId:Int,
        token:String
    ):Flow <DataState<Int>> = flow {


        val result=retrofitService.followingState(token=token,userId=userId)
        DataState.success(result)


    }

    private suspend fun getFollowersFromServer(
        token: String,
        page: Int,
        query: String
    ):List<Peoples>{
       return dtoMapper.toDomainList(retrofitService.getUserFollowers(token,page,query))

    }

}