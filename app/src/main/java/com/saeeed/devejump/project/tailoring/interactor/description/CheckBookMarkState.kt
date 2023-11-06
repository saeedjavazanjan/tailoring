package com.saeeed.devejump.project.tailoring.interactor.description

import android.annotation.SuppressLint
import android.util.Log
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.BookMarkedSewEntity
import com.saeeed.devejump.project.tailoring.cash.model.BookMarkedSewEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckBookMarkState (
    private val sewMethodDao: SewMethodDao,
    private val bookMarkMapper : BookMarkedSewEntityMapper
) {
    @SuppressLint("SuspiciousIndentation")
    fun execute(
        sewMethod:SewMethod
    ): Flow<DataState<Boolean>> = flow{
        try {


            val result= sewMethodDao.isBookMarkedOrNot(bookMarkMapper.mapFromDomainModel(sewMethod).id)
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