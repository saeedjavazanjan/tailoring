package com.saeeed.devejump.project.tailoring.interactor.register

import android.annotation.SuppressLint
import android.widget.Toast
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.RegisterUserDto
import com.saeeed.devejump.project.tailoring.network.model.RegisterUserPasswordDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import com.saeeed.devejump.project.tailoring.network.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import java.security.AccessController.getContext


class RegisterUser (
    private val retrofitService: RetrofitService,
    private val userDataMapper: UserDataMapper,
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: UserDataEntityMapper,

    ){


    @SuppressLint("SuspiciousIndentation")
    fun registerPasswordRequest(
        userName: String,
        phoneNumber: String,
        isNetworkAvailable: Boolean
    ):Flow<DataState<String?>> = flow {
        if (isNetworkAvailable) {
            emit(DataState.loading())

            try {
                val registerUser = RegisterUserDto(
                    userName = userName,
                    phoneNumber = phoneNumber
                )
                val response = retrofitService.registerPasswordRequest(registerUser)

                if (response.isSuccessful)
                    emit(DataState.success(response.body()))
                else {
                    /* if (response.code() == 429)
                {
                    emit(DataState.error("Too many Request"))
                }else {*/
                    try {
                        val errMsg = response.errorBody()?.string()?.let {
                            JSONObject(it).getString("error") // or whatever your message is
                        } ?: run {
                            emit(DataState.error(response.code().toString()))
                        }
                        emit(DataState.error(errMsg.toString()))
                    } catch (e: Exception) {
                        emit(DataState.error(e.message ?: "خطای سرور"))


                    }
                    // }
                }
                /* else {
                if (response.code()==429)
                    emit(DataState.error("Too many Request"))
                else
                emit(DataState.error(response.body()!!))
            }*/
            } catch (e: Exception) {

                emit(DataState.error(e.message ?: "خطای ناشناخته"))
            }
        }else{
            emit(DataState.error("شما به اینترنت دسترسی ندارید"))

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun loginPasswordRequest(
        phoneNumber: String,
        userName: String,
        isNetworkAvailable: Boolean
    ):Flow<DataState<String?>> = flow {
if (isNetworkAvailable) {
    emit(DataState.loading())
    try {
        val registerUser = RegisterUserDto(
            userName = userName,
            phoneNumber = phoneNumber
        )
        val response = retrofitService.loginPasswordRequest(registerUser)
        if (response.isSuccessful)
            emit(DataState.success(response.body()))
        else {
            /*  if (response.code() == 429)
                {
                    emit(DataState.error("Too many Request"))
                }else{*/
            try {
                val errMsg = response.errorBody()?.string()?.let {
                    JSONObject(it).getString("error") // or whatever your message is
                } ?: run {
                    emit(DataState.error(response.code().toString()))
                }
                emit(DataState.error(errMsg.toString()))
            } catch (e: Exception) {
                emit(DataState.error(e.message ?: "خطای سرور"))


            }
            //  }

        }
    } catch (e: Exception) {

        emit(DataState.error(e.message ?: "خطای ناشناخته"))
    }
}else{
    emit(DataState.error("شما به اینترنت دسترسی ندارید"))

}
    }

    fun loginPasswordCheck(
        phoneNumber: String,
        password:String,
        isNetworkAvailable: Boolean
    ):Flow<DataState<LoginResponse>> = flow {
        if(isNetworkAvailable) {
            emit(DataState.loading())
            try {
                val registerUserPasswordDto = RegisterUserPasswordDto(
                    userName = "",
                    phoneNumber = phoneNumber,
                    passWord = password

                )
                val result = retrofitService.loginPasswordCheck(registerUserPasswordDto)

                if (result.isSuccessful) {
                    emit(DataState.success(result.body()!!))
                    val user = mapToDomain(result.body()!!.userData!!)
                    try {
                        sewMethodDao.insertUserData(entityMapper.mapFromDomainModel(user))
                    } catch (e: Exception) {
                        emit(DataState.error(e.message ?: "خطای ذخیره کاربر"))

                    }
                } else {
                    try {
                        val errMsg = result.errorBody()?.string()?.let {
                            JSONObject(it).getString("error") // or whatever your message is
                        } ?: run {
                            emit(DataState.error(result.code().toString()))
                        }
                        emit(DataState.error(errMsg.toString()))
                    } catch (e: Exception) {
                        emit(DataState.error(e.message ?: "خطای سرور"))


                    }

                }

            } catch (e: Exception) {
                emit(DataState.error(e.message ?: "خطای ناشناخته"))
            }

        }else{
            emit(DataState.error("شما به اینترنت دسترسی ندارید"))

        }


    }

    fun registerPasswordCheck(
        phoneNumber: String,
        password:String,
        userName: String,
        isNetworkAvailable: Boolean
    ):Flow<DataState<LoginResponse>> = flow {
        if (isNetworkAvailable) {
            emit(DataState.loading())
            try {
                val registerUserPasswordDto = RegisterUserPasswordDto(
                    userName = userName,
                    phoneNumber = phoneNumber,
                    passWord = password

                )
                val result = retrofitService.registerPasswordCheck(registerUserPasswordDto)
                if (result.isSuccessful) {
                    emit(DataState.success(result.body()!!))

                    val user = mapToDomain(result.body()!!.userData!!)
                    try {
                        sewMethodDao.insertUserData(entityMapper.mapFromDomainModel(user))
                    } catch (e: Exception) {
                        emit(DataState.error(e.message ?: "خطای ذخیره کاربر"))

                    }

                } else {
                    try {
                        val errMsg = result.errorBody()?.string()?.let {
                            JSONObject(it).getString("error") // or whatever your message is
                        } ?: run {
                            emit(DataState.error(result.code().toString()))
                        }
                        emit(DataState.error(errMsg.toString()))
                    } catch (e: Exception) {
                        emit(DataState.error(e.message ?: "خطای سرور"))


                    }

                }

            } catch (e: Exception) {
                emit(DataState.error(e.message ?: "خطای ناشناخته"))
            }


        }else{
            emit(DataState.error("شما به اینترنت دسترسی ندارید"))

        }

    }

    fun mapToDomain(user:UserDataDto):UserData{
       return userDataMapper.mapToDomainModel(user)
    }

}