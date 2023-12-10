package com.saeeed.devejump.project.tailoring.interactor.user_profile

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import com.saeeed.devejump.project.tailoring.utils.USERID
import com.saeeed.devejump.project.tailoring.utils.userData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserProfileData(
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: SewEntityMapper,
    private val retrofitService: RetrofitService,
    private val dtoMapper: SewMethodMapper,
    private val userDataEntityMapper: UserDataEntityMapper,
    private val userDtoMapper:UserDataMapper

    ) {





    fun getUserData():
            Flow<DataState<UserData>> = flow {
        try {
            val userData =sewMethodDao.getUserData(USERID)
            emit(DataState.success(userDataEntityMapper.mapToDomainModel(userData)))
        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }

    fun getUserPosts(
        token: String,
        userId: Int,
        isNetworkAvailable: Boolean

    ):Flow<DataState<List<SewMethod>>> = flow {

            emit(DataState.loading())

        try {
            val sewMethods = getSewMethodsFromNetwork(
                token = token,
                userId = userId,
            )
            emit(DataState.success(sewMethods))
        }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.error(e.message.toString()))
        }




    }

    fun getUserBookMarkedPosts(
        token: String,
        userId: Int,
        isNetworkAvailable: Boolean

    ):Flow<DataState<List<SewMethod>>> = flow {

        emit(DataState.loading())

        try {
            val sewMethods = getSewMethodsFromNetwork(
                token = token,
                userId = userId,
            )
            emit(DataState.success(sewMethods))
        }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.error(e.message.toString()))
        }




    }

    private suspend fun getSewMethodsFromNetwork(
        token: String,
        userId: Int
    ): List<SewMethod> {

        return dtoMapper.toDomainList(
            retrofitService.search().sewmethods
            //  token = token,
            // page = page,
            //  userId = userId,
        )
    }

      fun updateUserData(
        userData: UserData?
    ):Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())
       val databaseUpdate= sewMethodDao.updateUserData(listOf(userDataEntityMapper.mapFromDomainModel(userData)))

        if (databaseUpdate>0){
            try {
             //   retrofitService.updateUseData(USERID,userDtoMapper.mapFromDomainModel(userData))
                emit(DataState.success(true))

            }catch (e:Exception){
                emit(DataState.success(false))

                emit(DataState.error("خطای ارسال اطلاعات"))

            }
        }else{
            emit(DataState.error("خطای به روز رسانی دیتا بیس"))
        }

    }
}