package com.saeeed.devejump.project.tailoring.interactor.description

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.BookMarkedSewEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.SewEntity
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookmarkPost(
    private val sewMethodDao: SewMethodDao,
    private val bookMarkMapper : BookMarkedSewEntityMapper
) {
    fun execute(
        post:SewMethod
    ): Flow<DataState<Long>> =flow{
        try {
           val result= sewMethodDao.insertBookMarkedSew(bookMarkMapper.mapFromDomainModel(post))
            emit(DataState.success(result))
        }catch (e:Exception){
            emit(DataState.error(e.message.toString()))
        }
    }
}