package com.saeeed.devejump.project.tailoring.presentation.ui.list

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.sew_list.SearchSew
import com.saeeed.devejump.project.tailoring.repository.SewRepository
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named



const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class ListViewModel
    @Inject constructor(
        private val searchSew: SearchSew,
        private val repository: SewRepository,
        private val restoreRecipes: RestoreSewMethods,
        @Named("auth_token") private val token: String,
        private val savedStateHandle: SavedStateHandle,

        ):ViewModel() {

    val methods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())
    val selectedCategory: MutableState<Category?> = mutableStateOf(null)
    var categoryScrollPosition: Float = 0F
    val loading = mutableStateOf(false)
    val page = mutableStateOf(1)
    var recipeListScrollPosition = 0

    val query = mutableStateOf("")

    init {
        newSearch()
    }

    private fun restoreState() {
        restoreRecipes.execute(page = page.value, query = query.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                methods.value = list
            }

            dataState.error?.let { error ->
               // dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    private fun newSearch() {
        Log.d(TAG, "newSearch: query: ${query.value}, page: ${page.value}")
        // New search. Reset the state
        resetSearchState()

        searchSew.execute(token = token, page = page.value, query = query.value,).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                methods.value = list
            }

            dataState.error?.let { error ->
                Log.e(TAG, "newSearch: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "nextPage: triggered: ${page.value}")

            if (page.value > 1) {
                searchSew.execute(token = token, page = page.value, query = query.value).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { list ->
                        methods.value = list
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "nextPage: ${error}")
                       // dialogQueue.appendErrorMessage("An Error Occurred", error)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
    private fun resetSearchState(){
        methods.value = listOf()
        if(selectedCategory.value?.value != query.value) clearSelectedCategory()
    }

    private fun clearSelectedCategory(){
        selectedCategory.value = null
    }
    fun onQueryChanged(query: String){
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String){
        val newCategory = getCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }
    fun onChangeCategoryScrollPosition(position: Float){
        categoryScrollPosition = position
    }
    private fun incrementPage(){
        setPage(page.value + 1)
    }

    private fun setListScrollPosition(position: Int){
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int){
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: Category?){
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String){
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}