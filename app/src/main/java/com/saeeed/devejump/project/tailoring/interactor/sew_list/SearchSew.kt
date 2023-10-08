package com.saeeed.devejump.project.tailoring.interactor.sew_list

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import com.saeeed.devejump.project.tailoring.utils.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchSew(
    private val sewMethodDao: SewMethodDao,
    private val retrofitService: RetrofitService,
    private val entityMapper: SewEntityMapper,
    private val dtoMapper: SewMethodMapper,
) {

    fun execute(
        token: String,
        page: Int,
        query: String,
    ): Flow<DataState<List<SewMethod>>> = flow {
        try {
            emit(DataState.loading())

           /* if (query == "error") {
                throw Exception("Search FAILED!")
            }*/

            try{
                // Convert: NetworkRecipeEntity -> Recipe -> RecipeCacheEntity
                val sewMethods = getSewMethodsFromNetwork(
                    token = token,
                    page = page,
                    query = query,
                )

                // insert into cache
                sewMethodDao.insertRecipes(entityMapper.toEntityList(sewMethods))
            }catch (e: Exception){
                // There was a network issue
                e.printStackTrace()
            }

            // query the cache
            val cacheResult = if (query.isBlank()) {
                sewMethodDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                sewMethodDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            // emit List<Recipe> from cache
            val list = entityMapper.fromEntityList(cacheResult)

            emit(DataState.success(list))
        } catch (e: Exception) {
            emit(DataState.error<List<SewMethod>>(e.message ?: "Unknown Error"))
        }
    }

    // WARNING: This will throw exception if there is no network connection
    private suspend fun getSewMethodsFromNetwork(
        token: String,
        page: Int,
        query: String
    ): List<SewMethod> {
        return dtoMapper.toDomainList(
            retrofitService.search(
                token = token,
                page = page,
                query = query,
            ).recipes
        )
    }
}