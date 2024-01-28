package com.saeeed.devejump.project.tailoring.interactor.description

import android.annotation.SuppressLint
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.PostEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.CommentMapper
import com.saeeed.devejump.project.tailoring.network.model.UpdatedCommentDto
import com.saeeed.devejump.project.tailoring.utils.USERID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserActivityOnPost (
    private val sewMethodDao: SewMethodDao,
    private val retrofitService: RetrofitService,
    private val entityMapper: UserDataEntityMapper,
    private val postEntityMapper: PostEntityMapper,
    private val commentDtoMapper:CommentMapper

) {

    @SuppressLint("SuspiciousIndentation")
    fun bookMark(
        postId:Int
    ): Flow<DataState<Int>> = flow{
        try {
         //  emit( DataState.loading())

       //  retrofitService.bookMark(USERID,postId)
        try {
          val bookMarks= getUserLocalBookMarks()
            bookMarks.add(postId.toString())
           val result= sewMethodDao.updateBookmarks(entityMapper.convertListToString(bookMarks), USERID)
            emit(DataState.success(result))
        }catch (e:Exception){
            e.printStackTrace()

        }

        }catch(e:Exception){
            emit(DataState.error(e.message.toString()))

        }

    }
    @SuppressLint("SuspiciousIndentation")
    fun unBookMark(
        postId:Int
    ): Flow<DataState<Int>> = flow{
        try {
          //  emit( DataState.loading())

            //  retrofitService.bookMark(USERID,postId)
            try {
                val bookMarks= getUserLocalBookMarks()
                bookMarks.remove(postId.toString())
                val result= sewMethodDao.updateBookmarks(entityMapper.convertListToString(bookMarks), USERID)
                emit(DataState.success(result))
            }catch (e:Exception){
                e.printStackTrace()

            }

        }catch(e:Exception){
            emit(DataState.error(e.message.toString()))

        }

    }
    fun checkBookMarkState(postId:Int):Flow<DataState<Boolean>> = flow {
        try {
            if (getUserLocalBookMarks().contains(postId.toString()))
                emit(DataState.success(true))
            else emit( DataState.success(false))

        }catch (e:Exception){
           emit( DataState.error(e.message.toString()))
        }


    }
    fun checkLikeState(postId:Int):Flow<DataState<Boolean>> = flow {
        try {
            if (getUserLocalLikes().contains(postId.toString()))
                emit(DataState.success(true))
            else emit( DataState.success(false))

        }catch (e:Exception){
            emit( DataState.error(e.message.toString()))
        }


    }

    @SuppressLint("SuspiciousIndentation")
    fun likePost(
        postId:Int,
        likeCount:String
    ): Flow<DataState<Int>> = flow{
        try {

            //  retrofitService.likePost(USERID,postId)
            try {
                val likes= getUserLocalLikes()
                likes.add(postId.toString())
                val result= sewMethodDao.updateLikes(entityMapper.convertListToString(likes), USERID)
                    val insertResult=sewMethodDao.updatePostLikeCount(likeCount,postId)
                    emit(DataState.success(result))


           //     emit(DataState.success(insertResult))

            }catch (e:Exception){
                e.printStackTrace()
                emit(DataState.error(e.message.toString()))

            }

        }catch(e:Exception){
            emit(DataState.error(e.message.toString()))

        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun unLikePost(
        postId:Int,
        likeCount: String
    ): Flow<DataState<Int>> = flow{
        try {

            //  retrofitService.likePost(USERID,postId)
            try {
                val likes= getUserLocalLikes()
                likes.remove(postId.toString())
                val result= sewMethodDao.updateLikes(entityMapper.convertListToString(likes), USERID)
                    val insertResult=sewMethodDao.updatePostLikeCount(likeCount,postId)
                    emit(DataState.success(result))

            }catch (e:Exception){
                e.printStackTrace()

            }

        }catch(e:Exception){
            emit(DataState.error(e.message.toString()))

        }

    }
    suspend fun getUserLocalBookMarks():MutableList<String>{

        val bookMarks=sewMethodDao.getUserData(USERID).bookMarks

       return entityMapper.convertStringToList(bookMarks)

    }

    suspend fun getUserLocalLikes():MutableList<String>{

        val likes=sewMethodDao.getUserData(USERID).liKes

        return entityMapper.convertStringToList(likes)

    }

     fun commentOnPost(comment: Comment):Flow<DataState<String?>> = flow{
        try {
            emit (DataState.loading())
           val response= retrofitService.commentOnPost(commentDtoMapper.mapFromDomainModel(comment))

          if(response.isSuccessful){
              emit(DataState.success(response.body()))

          }else{
              emit(DataState.error(response.message().toString()))

          }


        }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.error(e.message.toString()))
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun editComment(comment: Comment):Flow<DataState<String?>> = flow{

        val updatedComment=UpdatedCommentDto(
            commentText=comment.comment,
            date = System.currentTimeMillis()
        )
        try {
            emit (DataState.loading())
         val response= retrofitService.editComment(comment.id, updatedComment)
            if(response.isSuccessful){
                emit(DataState.success(response.body()))

            }else{
                emit(DataState.error(response.message().toString()))

            }
        }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.error(e.message.toString()))
        }

    }

    fun reportComment(comment: Comment,commentId:Int):Flow<DataState<Int>> = flow{

        //   retrofitService.editComment(USERID,postId,comment.comment)


        emit(DataState.success(1))


    }

    fun removeComment(commentId:Int):Flow<DataState<Int?>> = flow{


             // retrofitService.removeComment(USERID,postId,comment.comment)
try {
    emit (DataState.loading())
    val response=  retrofitService.removeComment(commentId = commentId)
    if(response.code()==204){
        emit(DataState.success(response.body()))

    }else{
        emit(DataState.error(response.message().toString()))
    }
}catch (e:Exception){
    e.printStackTrace()
    emit(DataState.error(e.message.toString()))
}



    }
  /*  suspend fun getPostComments(postId:Int):MutableList<Comment>{

        val comments=sewMethodDao.getSewById(postId)!!.comments

        return sewEntityMapper.convertCommentsStringToList(comments)

    }

    suspend fun getUserComments(userId:Int,postId: Int,comment: Comment):MutableList<CommentOnSpecificPost>{


        val comments=sewMethodDao.getUserData(userId)!!.comments
        val userCommentsOnPosts=entityMapper.convertStringToCommentList(comments)
        var commentsOnThisPost:CommentOnSpecificPost?=null
        try {
            try {
                commentsOnThisPost=userCommentsOnPosts.first {
                    it.postId==postId.toString()
                }
            }catch (e:Exception){
                userCommentsOnPosts.add(
                    CommentOnSpecificPost(postId.toString(), mutableListOf(comment)))
                commentsOnThisPost=userCommentsOnPosts[0]
            }
            commentsOnThisPost.let {
                it!!.comments.add(comment)
                userCommentsOnPosts.add(it)
            }



}catch (e:Exception){
    e.printStackTrace()

}

        return userCommentsOnPosts

    }
*/
}