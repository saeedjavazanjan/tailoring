package com.saeeed.devejump.project.tailoring.interactor.description

import android.annotation.SuppressLint
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.CommentEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.CommentDto
import com.saeeed.devejump.project.tailoring.network.model.CommentMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class GetComments(
    private val sewMethodDao: SewMethodDao,
    private val commentEntityMapper: CommentEntityMapper,
    private val retrofitService: RetrofitService,
    private val commentMapper: CommentMapper


) {

    @SuppressLint("SuspiciousIndentation")
    fun getPostComments(
        postId:Int,
        isNetworkAvailable: Boolean

    ): Flow<DataState<List<Comment>>> = flow {

             emit(DataState.loading())
        try {
            val response= getCommentsFromNetwork(postId)?.let {
                commentMapper.toDomainList(it)
            }
            emit(DataState.success(response!!))
        }catch (e:Exception){
            emit(DataState.error(e.message?:"خطای ناشناخته"))
        }


        //for caching comments
      //  try {
      /*      comments.forEach{
                sewMethodDao.insertComment(commentEntityMapper.mapFromDomainModel(it))
            }
        val cashResult=sewMethodDao.getPostWithComment(postId)

        cashResult.forEach {
            emit(DataState.success( commentEntityMapper.fromEntityList(it.comments)))
        }*/
     /*   }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.error(e.message.toString()))

        }*/





            //   emit(DataState.success(commentMapper.toDomainList(commentsFromNetwork)))




    }
    /*
        fun getComments(
            postId: Int,
            token: String,
            isNetworkAvailable: Boolean,
        ):Flow<DataState<List<Comment>>> = flow {

            try {
                emit(DataState.loading())

                var comments=getCommentsFromNetwork(token,postId)

                emit(DataState.success(comments))
            }catch (e:Exception){
                e.printStackTrace()
                emit(DataState.error(e.message.toString()))

            }

    */
    /*
                try {
                    comments.forEach{
                        sewMethodDao.insertComment(commentEntityMapper.mapFromDomainModel(it))
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    emit(DataState.error(e.message.toString()))

                }*//*

         //   var cashResult=sewMethodDao.getPostWithComment(postId)

           // cashResult.forEach {
              //emit(DataState.success(commentEntityMapper.fromEntityList(  it.comments)))

            //}



    }
*/
    @SuppressLint("SuspiciousIndentation")
    private suspend fun getCommentsFromNetwork( postId: Int): List<CommentDto>? {

        try {
          val response= retrofitService.onePostComments(postId)
            if (response.isSuccessful){
                return response.body()
            }else {
                return emptyList()
            }

        }catch (e:Exception){
            return emptyList()
        }
    }

}
