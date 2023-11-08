package com.saeeed.devejump.project.tailoring.interactor.description

import android.annotation.SuppressLint
import android.util.Log
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserActivityOnPost(
    private val sewMethodDao: SewMethodDao,
  //  private val bookMarkMapper : BookMarkedSewEntityMapper
) {
    fun bookMark(
        id:Int,
        bookMarkState:Boolean
    ): Flow<DataState<Int>> =flow{
        try {
           val result= sewMethodDao.updateBookmarkState(bookMarkState,id)
            emit(DataState.success(result))
        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }

   /* fun remove(
        post:SewMethod
    ): Flow<DataState<Int>> =flow{
        try {
            val result= sewMethodDao.deleteSew(listOf( bookMarkMapper.mapFromDomainModel(post).id))
            emit(DataState.success(result))
        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }*/
    @SuppressLint("SuspiciousIndentation")
    fun cheCkBookMarkState(
        id:Int
    ): Flow<DataState<Boolean>> = flow{
        try {
            val result= sewMethodDao.isBookMarkedOrNot(id)
            try {
                emit(DataState.success(result))

            }catch (e:Exception){
                Log.e(TAG,"ceckBockmark${e.message.toString()}")

            }

        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }
}