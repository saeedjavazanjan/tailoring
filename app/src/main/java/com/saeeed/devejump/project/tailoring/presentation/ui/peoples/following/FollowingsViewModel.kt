package com.saeeed.devejump.project.tailoring.presentation.ui.peoples.following

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Peoples
import com.saeeed.devejump.project.tailoring.interactor.followings.GetFollowingsList
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.presentation.ui.search.STATE_KEY_QUERY
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.POSTS_PAGINATION_PAGE_SIZE
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
class FollowingsViewModel
   @Inject
       constructor(
       private val getFollowingsList: GetFollowingsList,
       private val restoreSewMethods: RestoreSewMethods,
       private val connectivityManager: ConnectivityManager,
       @Named("auth_token") private val token: String,
       private val savedStateHandle: SavedStateHandle,
       ):ViewModel() {

    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val followings: MutableState<List<Peoples>> = mutableStateOf(ArrayList())
    val page = mutableStateOf(1)
    val query = mutableStateOf("")
    var scrollPosition = 0

    val followingStatus= mutableStateOf(false)

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        if(scrollPosition != 0){
            onTriggerEvent(FollowingsEvent.RestoreStateEvent)
        }
        else{
            onTriggerEvent(FollowingsEvent.NewEvent)
        }

    }

    fun onTriggerEvent(event: FollowingsEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is FollowingsEvent.NewEvent -> {
                          newSearch()
                    }
                    is FollowingsEvent.NextPageEvent -> {
                           nextPage()
                    }
                    is FollowingsEvent.RestoreStateEvent -> {
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
        getFollowingsList.restoreFollowers(page = page.value, query = query.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                followings.value = list
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

        getFollowingsList.execute(
            token = token,
            page = page.value,
            query = query.value,
            connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                followings.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ دااده است",it.message.toString())
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {
        if ((scrollPosition + 1) >= (page.value * POSTS_PAGINATION_PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "nextPage: triggered: ${page.value}")

            if (page.value > 1) {
                getFollowingsList.execute(
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
        val current = ArrayList(this.followings.value)
        current.addAll(follower)
        this.followings.value = current
    }

    private fun incrementPage(){
        setPage(page.value + 1)
    }

    private fun resetSearchState(){
        followings.value = listOf()
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



    fun stateOfFollowing(userId:Int,token: String){
        getFollowingsList.checkIfUserFollowed(userId,token).onEach { dataState ->

            dataState.data?.let {
                if (it==204){
                    followingStatus.value=true
                }else if (it==404){
                    followingStatus.value=true

                }
            }

        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())

        }

    }









}