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

class GetComments(
    private val sewMethodDao: SewMethodDao,
    private val commentEntityMapper: CommentEntityMapper,
    private val retrofitService: RetrofitService,
    private val commentMapper: CommentMapper


) {

    @SuppressLint("SuspiciousIndentation")
    fun getPostComments(
        postId:Int,
        token: String,
        isNetworkAvailable: Boolean

    ): Flow<DataState<List<Comment>>> = flow {

         //    emit(DataState.loading())


              val comments= commentMapper.toDomainList(getCommentsFromNetwork(token,postId))
      //  try {
            comments.forEach{
                sewMethodDao.insertComment(commentEntityMapper.mapFromDomainModel(it))
            }
        val cashResult=sewMethodDao.getPostWithComment(postId)

        cashResult.forEach {
            emit(DataState.success( commentEntityMapper.fromEntityList(it.comments)))
        }
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
    private suspend fun getCommentsFromNetwork(token: String, postId: Int): List<CommentDto> {

        try {
            return retrofitService.onePostComments(postId)

        }catch (e:Exception){
            return emptyList()
        }
    }

}
