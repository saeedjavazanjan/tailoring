package com.saeeed.devejump.project.tailoring.interactor.user_profile

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

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
                if(isNetworkAvailable) {

                    emit(DataState.loading())
                    val userData = getUserDataFromNetwork(token)
                    emit(DataState.success(userData))
                }
            }catch (e:Exception){
                e.printStackTrace()
                emit(DataState.error(e.message?:"خطایی رخ داده است"))

            }





    }

    fun getAuthorData(
    ):
            Flow<DataState<UserData>> = flow {
        try {
            val userData =sewMethodDao.getUserData()
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

            val sewMethods = getSewMethodsFromNetwork(
                token = token,
                userId = userId,
            )
            emit(DataState.success(sewMethods))
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
            retrofitService.search(1,"",30)
            //  token = token,
            // page = page,
            //  userId = userId,
        )
    }

      @SuppressLint("SuspiciousIndentation")
      fun updateUserData(
        userData: UserData?,
        token: String?,
        context: Context,
        avatar: Uri
    ):Flow<DataState<String>> = flow {

        val part=  getFileFromUri(
              context,
              avatar
          )

        emit(DataState.loading())
     //  val databaseUpdate= sewMethodDao.updateUserData(listOf(userDataEntityMapper.mapFromDomainModel(userData)))

       // if (databaseUpdate>0){
            try {
              val result=  retrofitService.updateUseData(
                    token,
                    userData!!.userName.toRequestBody(),
                    userData.bio.toRequestBody(),
                    part
                )
                if (result.isSuccessful)
                emit(DataState.success("به روز رسانی با موفقیت انجام شد"))
                else{
                    try {
                        val errMsg = result.errorBody()?.string()?.let {
                            JSONObject(it).getString("error") // or whatever your message is
                        } ?: run {
                            emit(DataState.error( result.code().toString()))
                        }
                        emit(DataState.error(errMsg.toString()))
                    }catch (e:Exception){
                        emit(DataState.error("خطای سرور"))


                    }

                }

           }catch (e:Exception){
                emit(DataState.error("خطای ارسال اطلاعات"))

            }
      /* }else{
            emit(DataState.error("خطای به روز رسانی دیتا بیس"))
        }*/

    }
    fun checkIfUserFollowed(
        userId:Int,
        token:String
    ):Flow <DataState<Int>> = flow {


        val result=retrofitService.followingState(token=token,userId=userId)
        DataState.success(result)


    }

    private suspend fun getUserDataFromNetwork(token:String): UserData {

        return userDtoMapper.mapToDomainModel(retrofitService.userData(token).body()!!)


    }


    fun getFileFromUri(
        context: Context,
        avatarUri: Uri
    ): MultipartBody.Part {
        val fileDire = context.filesDir
        val file = File(fileDire, "image.png")
        val inputStream = context.contentResolver.openInputStream(avatarUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("AvatarFile", file.name, requestBody)
    }
}