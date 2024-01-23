package com.saeeed.devejump.project.tailoring.presentation.ui.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.sew_list.SearchSew
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.POSTS_PAGINATION_PAGE_SIZE
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named



const val STATE_KEY_PAGE = "sew.state.page.key"
const val STATE_KEY_QUERY = "sew.state.query.key"
const val STATE_KEY_LIST_POSITION = "sew.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "sew.state.query.selected_category"

@HiltViewModel
class ListViewModel
    @Inject constructor(
        private val searchSew: SearchSew,
        private val restoreSewMethods: RestoreSewMethods,
        private val connectivityManager: ConnectivityManager,
        @Named("auth_token") private val token: String,
        private val savedStateHandle: SavedStateHandle,
        ):ViewModel() {

    val methods: MutableState<List<Post>> = mutableStateOf(ArrayList())
    val selectedCategory: MutableState<Category?> = mutableStateOf(null)
   // var categoryScrollPosition: Float = 0F
    val loading = mutableStateOf(false)
    val page = mutableStateOf(1)
    var categoryScrollPosition = 0

    val query = mutableStateOf("")

    val dialogQueue = DialogQueue()

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }
        savedStateHandle.get<Category>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        if(categoryScrollPosition != 0){
            onTriggerEvent(SearchEvent.RestoreStateEvent)
        }
        else{
            onTriggerEvent(SearchEvent.NewSearchEvent)
        }
    }

    fun onTriggerEvent(event: SearchEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is SearchEvent.NewSearchEvent -> {
                        newSearch()
                    }
                    is SearchEvent.NextPageEvent -> {
                        nextPage()
                    }
                    is SearchEvent.RestoreStateEvent -> {
                        restoreState()
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
            finally {
                Log.d(TAG, "launchJob: finally called.")
            }
        }
    }

    private fun restoreState() {
        restoreSewMethods.execute(page = page.value, query = query.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                methods.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

     fun newSearch() {
        Log.d(TAG, "newSearch: query: ${query.value}, page: ${page.value}")
        // New search. Reset the state
        resetSearchState()

        searchSew.execute(
            token = token,
            page = page.value,
            query = query.value,
            connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                methods.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {
        if ((categoryScrollPosition + 1) >= (page.value * POSTS_PAGINATION_PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "nextPage: triggered: ${page.value}")

            if (page.value > 1) {
                searchSew.execute(
                    token = token,
                    page = page.value,
                    query = query.value,
                    connectivityManager.isNetworkAvailable.value).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { list ->
                    appendMethods(list)

                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "nextPage: ${error}")
                        dialogQueue.appendErrorMessage("An Error Occurred", error)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun appendMethods(methods: List<Post>){
        val current = ArrayList(this.methods.value)
        current.addAll(methods)
        this.methods.value = current
    }

    private fun incrementPage(){
        setPage(page.value + 1)
    }

    private fun resetSearchState(){
        methods.value = listOf()
        if(selectedCategory.value?.value != query.value)
            clearSelectedCategory()

    }

    private fun clearSelectedCategory(){
        setSelectedCategory(null)
        selectedCategory.value = null
    }
    fun onQueryChanged(query: String){
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String){
        val newCategory = getCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
        setPage(1)

    }
    fun onChangeCategoryScrollPosition(position: Int){
        setListScrollPosition(position = position)
    }
    private fun setListScrollPosition(position: Int){
        categoryScrollPosition = position
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