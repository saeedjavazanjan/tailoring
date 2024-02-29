package com.saeeed.devejump.project.tailoring.interactor.user_profile

import android.annotation.SuppressLint
import android.net.Uri
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.PostEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostDto
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import com.saeeed.devejump.project.tailoring.utils.GetFileOfUri
import com.saeeed.devejump.project.tailoring.utils.posts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class GetUserProfileData(
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: PostEntityMapper,
    private val retrofitService: RetrofitService,
    private val dtoMapper: PostMapper,
    private val userDataEntityMapper: UserDataEntityMapper,
    private val userDtoMapper:UserDataMapper,
    private val getFileOfUri: GetFileOfUri,

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
        isNetworkAvailable: Boolean

    ):Flow<DataState<List<Post>>> = flow {

        if(isNetworkAvailable) {
            emit(DataState.loading())

            val result = getUserPostsFromNetwork(
                token = token
            )

            if (result.isSuccessful) {
                emit(DataState.success(dtoMapper.toDomainList(result.body()!!)))

            } else if (result.code() == 401) {

                emit(DataState.error("شما دسترسی لازم را ندارید"))
            } else {
                try {
                    val errMsg = result.errorBody()?.string()?.let {
                        JSONObject(it).getString("error") // or whatever your message is
                    } ?: run {
                        emit(DataState.error(result.code().toString()))
                    }
                    emit(DataState.error(errMsg.toString()))
                } catch (e: Exception) {
                    emit(DataState.error("خطای سرور"))


                }

            }
        }

    }

    fun getUserBookMarkedPosts(
        token: String,
        userId: Int,
        isNetworkAvailable: Boolean

    ):Flow<DataState<List<Post>>> = flow {

        emit(DataState.loading())

        val result = getUserPostsFromNetwork(
            token = token)

        if (result.isSuccessful){
            emit(DataState.success(dtoMapper.toDomainList(result.body()!!)))

        }    else if (result.code()==401){

            emit(DataState.error("شما دسترسی لازم را ندارید"))
        }
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




    }

    private suspend fun getUserPostsFromNetwork(
        token: String,
    ): Response<List<PostDto>> {

        return retrofitService.getUserPosts(
                token = token, 1,30)

    }

      @SuppressLint("SuspiciousIndentation")
      fun updateUserData(
        userData: UserData?,
        token: String?,
        avatar: Uri,
        isNetworkAvailable: Boolean

      ):Flow<DataState<String>> = flow {
if (isNetworkAvailable) {
    val part = getFileOfUri.getImageFileFromUri(avatar)

    emit(DataState.loading())
    //  val databaseUpdate= sewMethodDao.updateUserData(listOf(userDataEntityMapper.mapFromDomainModel(userData)))

    // if (databaseUpdate>0){
    try {
        val result = retrofitService.updateUseData(
            token,
            userData!!.userName.toRequestBody(),
            userData.bio.toRequestBody(),
            part
        )
        if (result.isSuccessful)
            emit(DataState.success("به روز رسانی با موفقیت انجام شد"))
        else if (result.code() == 401) {

            emit(DataState.error("شما دسترسی لازم را ندارید"))
        } else {
            try {
                val errMsg = result.errorBody()?.string()?.let {
                    JSONObject(it).getString("error") // or whatever your message is
                } ?: run {
                    emit(DataState.error(result.code().toString()))
                }
                emit(DataState.error(errMsg.toString()))
            } catch (e: Exception) {
                emit(DataState.error("خطای سرور"))


            }

        }

    } catch (e: Exception) {
        emit(DataState.error("خطای ارسال اطلاعات"))

    }
    /* }else{
            emit(DataState.error("خطای به روز رسانی دیتا بیس"))
        }*/
}else{
    emit(DataState.error("شما به اینترنت دسترسی ندارید"))

}
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

}