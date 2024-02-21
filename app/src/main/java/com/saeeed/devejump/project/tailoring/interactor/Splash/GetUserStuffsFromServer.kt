package com.saeeed.devejump.project.tailoring.interactor.Splash

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import com.saeeed.devejump.project.tailoring.utils.USERID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response

class GetUserStuffsFromServer(
    private val sewMethodDao: SewMethodDao,
    private val dtoMapper: UserDataMapper,
    private val retrofitService: RetrofitService,
    private val entityMapper: UserDataEntityMapper,

    ) {


    fun execute(
        token:String
    ): Flow<DataState<UserData>> = flow {

        try {
            emit(DataState.loading())

            try {
                val getedUserData=getUserDataFromNetwork(token)
                getedUserData.let {
                    val receivedUser=dtoMapper.mapToDomainModel(
                        it.body()!!
                    )
                  //  receivedUser.avatar="http://10.0.2.2:5198/Avatars/${it.body()!!.avatar}"

                    if (getedUserData.isSuccessful) {
                        sewMethodDao.insertUserData(
                            entityMapper.mapFromDomainModel(
                                receivedUser
                            )
                        )
                        emit(DataState.success(dtoMapper.mapToDomainModel(it.body()!!)))
                    }
                    else {
                        try {
                            val errMsg = getedUserData.errorBody()?.string()?.let {
                                JSONObject(it).getString("error") // or whatever your message is
                            } ?: run {
                                emit(DataState.error(getedUserData.code().toString()))
                            }
                            emit(DataState.error(errMsg.toString()))
                        } catch (e: Exception) {
                            emit(DataState.error(e.message ?: "خطای سرور"))


                        }
                    }

                }

            }catch (e:Exception){
                e.printStackTrace()
                emit(DataState.error(e.message?:"خطای ناشناخته"))

            }
          //  val savedUserData=entityMapper.mapToDomainModel(sewMethodDao.getUserData(USERID))
        }catch (e:Exception){

            emit(DataState.error(e.message?:"خطای ناشناخته"))
        }




    }

    private suspend fun getUserDataFromNetwork(token:String): Response<UserDataDto> {

        return retrofitService.userData(token)


    }


}