package com.saeeed.devejump.project.tailoring.interactor.Splash

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import com.saeeed.devejump.project.tailoring.utils.USERID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserStuffsFromServer(
    private val sewMethodDao: SewMethodDao,
    private val dtoMapper: UserDataMapper,
    private val retrofitService: RetrofitService,
    private val entityMapper: UserDataEntityMapper,

    ) {


    fun execute(userId:Int): Flow<DataState<UserData>> = flow {

        try {
            emit(DataState.loading())

            try {
                val getedUserData=getUserDataFromNetwork(userId)
                getedUserData.let {

                        sewMethodDao.insertUserData(entityMapper.mapFromDomainModel(it))


                }

            }catch (e:Exception){
                e.printStackTrace()

            }
            val savedUserData=entityMapper.mapToDomainModel(sewMethodDao.getUserData(USERID))
            emit(DataState.success(savedUserData))
        }catch (e:Exception){

            emit(DataState.error(e.message.toString()))
        }




    }

    private suspend fun getUserDataFromNetwork(UserId:Int): UserData {

        return dtoMapper.mapToDomainModel(retrofitService.userData(UserId))


    }


}