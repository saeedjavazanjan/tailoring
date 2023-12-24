package com.saeeed.devejump.project.tailoring.interactor.user_profile

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.PostEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserProfileData(
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: PostEntityMapper,
    private val retrofitService: RetrofitService,
    private val dtoMapper: PostMapper,
    private val userDataEntityMapper: UserDataEntityMapper,
    private val userDtoMapper:UserDataMapper

    ) {



    fun getUserData(
        token: String,
        userId: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<UserData>> = flow {



            try {
                emit(DataState.loading())
                val userData=getUserDataFromNetwork(userId)
                emit(DataState.success(userData))

            }catch (e:Exception){
                e.printStackTrace()
                emit(DataState.error(e.message?:"خطایی رخ داده است"))

            }





    }

    fun getAuthorData(
        token: String,
        userId: Int,
        isNetworkAvailable: Boolean

    ):
            Flow<DataState<UserData>> = flow {
        try {
            val userData =sewMethodDao.getUserData(userId)
            emit(DataState.success(userDataEntityMapper.mapToDomainModel(userData)))
        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }

    fun getUserPosts(
        token: String,
        userId: Int,
        isNetworkAvailable: Boolean

    ):Flow<DataState<List<Post>>> = flow {

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

    ):Flow<DataState<List<Post>>> = flow {

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
    ): List<Post> {

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

                emit(DataState.error(e.message?:"خطای ارسال اطلاعات"))

            }
        }else{
            emit(DataState.error("خطای به روز رسانی دیتا بیس"))
        }

    }
    fun checkIfUserFollowed(
        userId:Int,
        token:String
    ):Flow <DataState<Int>> = flow {


        val result=retrofitService.followingState(token=token,userId=userId)
        DataState.success(result)


    }

    private suspend fun getUserDataFromNetwork(UserId:Int): UserData {

        return userDtoMapper.mapToDomainModel(retrofitService.userData(UserId))


    }

}