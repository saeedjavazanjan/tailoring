package com.saeeed.devejump.project.tailoring.interactor.sew_list

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.utils.POSTS_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreSewMethods(
    private val sewMethodDao: SewMethodDao,
    private val entityMapper: SewEntityMapper,
) {
    fun execute(
        page: Int,
        query: String
    ): Flow<DataState<List<SewMethod>>> = flow {
        try {
            emit(DataState.loading())
            // query the cache
            val cacheResult = if (query.isBlank()){
                sewMethodDao.restoreAllSewMethods(
                    pageSize = POSTS_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }
            else{
                sewMethodDao.restoreSewMethods(
                    query = query,
                    pageSize = POSTS_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            // emit List<Recipe> from cache
            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        }catch (e: Exception){
            emit(DataState.error<List<SewMethod>>(e.message?: "Unknown Error"))
        }
    }
}