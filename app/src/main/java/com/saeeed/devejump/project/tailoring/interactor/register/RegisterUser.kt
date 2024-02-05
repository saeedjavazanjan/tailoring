package com.saeeed.devejump.project.tailoring.interactor.register

import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.RegisterUserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUser (
    private val retrofitService: RetrofitService,
    ){


    fun registerPasswordRequest(
        userName: String,
        phoneNumber: String
    ):Flow<DataState<String?>> = flow {
        emit(DataState.loading())

        try {
            val registerUser=RegisterUserDto(
                userName=userName,
                phoneNumber=phoneNumber
            )
           val response= retrofitService.registerPasswordRequest(registerUser)

            if(response.isSuccessful)
                emit(DataState.success(response.body()))
            else
                emit(DataState.error(response.body()!!))
        }catch (e:Exception){

                emit(DataState.error(e.message?:"خطای ناشناخته"))
        }

    }

    fun loginPasswordRequest(
        phoneNumber: String,
        userName: String
    ):Flow<DataState<String?>> = flow {

    emit(DataState.loading())
        try {
            val registerUser=RegisterUserDto(
                userName=userName,
                phoneNumber=phoneNumber
            )
            val response=retrofitService.loginPasswordRequest(registerUser)
            if(response.isSuccessful)
                emit(DataState.success(response.body()))
            else
                emit(DataState.error("خطای ارسال"))
        }catch (e:Exception){

            emit(DataState.error(e.message?:"خطای ناشناخته"))
        }

    }



}