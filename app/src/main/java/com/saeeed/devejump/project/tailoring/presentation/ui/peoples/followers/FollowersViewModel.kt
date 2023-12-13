package com.saeeed.devejump.project.tailoring.presentation.ui.peoples.followers

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Peoples
import com.saeeed.devejump.project.tailoring.interactor.followers.GetFollowersList
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.presentation.ui.search.PAGE_SIZE
import com.saeeed.devejump.project.tailoring.presentation.ui.search.STATE_KEY_QUERY
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
const val STATE_KEY_PAGE = "people.state.page.key"
const val STATE_KEY_LIST_POSITION = "people.state.query.list_position"
const val STATE_KEY_QUERY = "people.state.query.key"

@HiltViewModel
class FollowersViewModel
   @Inject
       constructor(
       private val getFollowersList: GetFollowersList,
       private val restoreSewMethods: RestoreSewMethods,
       private val connectivityManager: ConnectivityManager,
       @Named("auth_token") private val token: String,
       private val savedStateHandle: SavedStateHandle,
       ):ViewModel() {

    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val followers: MutableState<List<Peoples>> = mutableStateOf(ArrayList())
    val page = mutableStateOf(1)
    val query = mutableStateOf("")
    var scrollPosition = 0


    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        if(scrollPosition != 0){
            onTriggerEvent(FollowersEvent.RestoreStateEvent)
        }
        else{
            onTriggerEvent(FollowersEvent.NewEvent)
        }

    }

    fun onTriggerEvent(event: FollowersEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is FollowersEvent.NewEvent -> {
                          newSearch()
                    }
                    is FollowersEvent.NextPageEvent -> {
                           nextPage()
                    }
                    is FollowersEvent.RestoreStateEvent -> {
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
        getFollowersList.restoreFollowers(page = page.value, query = query.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                followers.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    private fun newSearch() {
        Log.d(TAG, "newSearch: query: ${query.value}, page: ${page.value}")
        // New search. Reset the state
        resetSearchState()

        getFollowersList.execute(
            token = token,
            page = page.value,
            query = query.value,
            connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                followers.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ دااده است",it.message.toString())
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {
        if ((scrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "nextPage: triggered: ${page.value}")

            if (page.value > 1) {
                getFollowersList.execute(
                    token = token,
                    page = page.value,
                    query = query.value,
                    connectivityManager.isNetworkAvailable.value).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { list ->
                        appendFollowers(list)

                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "nextPage: ${error}")
                        dialogQueue.appendErrorMessage("An Error Occurred", error)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun appendFollowers(follower: List<Peoples>){
        val current = ArrayList(this.followers.value)
        current.addAll(follower)
        this.followers.value = current
    }

    private fun incrementPage(){
        setPage(page.value + 1)
    }

    private fun resetSearchState(){
        followers.value = listOf()
    }

    fun onQueryChanged(query: String){
        setQuery(query)
    }

    fun onChangeScrollPosition(position: Int){
        setListScrollPosition(position = position)
    }
    private fun setListScrollPosition(position: Int){
        scrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }



    private fun setPage(page: Int){
        this.page.value = page
        savedStateHandle.set(com.saeeed.devejump.project.tailoring.presentation.ui.search.STATE_KEY_PAGE, page)
    }

    private fun setQuery(query: String){
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }













}