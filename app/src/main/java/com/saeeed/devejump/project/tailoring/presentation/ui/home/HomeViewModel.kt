package com.saeeed.devejump.project.tailoring.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.home.Bests
import com.saeeed.devejump.project.tailoring.interactor.home.GetHomeData
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.presentation.ui.search.PAGE_SIZE
import com.saeeed.devejump.project.tailoring.presentation.ui.search.STATE_KEY_LIST_POSITION
import com.saeeed.devejump.project.tailoring.presentation.ui.search.SearchEvent
import com.saeeed.devejump.project.tailoring.utils.BEST_OF_DAY
import com.saeeed.devejump.project.tailoring.utils.BEST_OF_MONTH
import com.saeeed.devejump.project.tailoring.utils.BEST_OF_WEEK
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.GET_HOME_BANNERS
import com.saeeed.devejump.project.tailoring.utils.TAG
import com.saeeed.devejump.project.tailoring.utils.USERID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


const val STATE_KEY_PAGE = "sew.state.page.key"

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val bests: Bests,
    private val getHomeData: GetHomeData,
    private val restoreSewMethods: RestoreSewMethods,
    private val connectivityManager: ConnectivityManager,
    @Named("auth_token") private val token: String,
    private val savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    val banners: MutableState<List<Banner>> = mutableStateOf(ArrayList())
    val methods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())

    private val bannersQuery = GET_HOME_BANNERS
    private val bestsOfMonthQuery = BEST_OF_MONTH
    private val bestOfWeekQuery = BEST_OF_WEEK
    private val bestOfDayQuery = BEST_OF_DAY

    val bestOfMonthMethods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())
    val bestOfWeekMethods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())
    val bestOfDayMethods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())


    val page = mutableStateOf(1)
    private var scrollPosition = 0


    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()

    init {
        getBanners()


        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }

            onTriggerEvent(FollowingsPostsEvent.NewLoad)

    }

    fun onTriggerEvent(event:FollowingsPostsEvent) {
        viewModelScope.launch {
            try {
                when(event){
                    is FollowingsPostsEvent.NewLoad -> {
                        getFollowingsPost()
                    }
                    is FollowingsPostsEvent.NextPageEvent -> {
                           nextPage()
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

    private fun getBanners() {
        Log.d(TAG, "getbanner: query: ${bannersQuery}")

        getHomeData.getBanner(
            token = token,
            query = bannersQuery,
            connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { list ->
                banners.value = list

            }

            dataState.error?.let { error ->
                // dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    private fun getBestsOfMonth() {
        Log.d(TAG, "bestOfMonth: query: ${bestsOfMonthQuery}")
        // New search. Reset the state
        bests.execute(
            token = token,
            query = bestsOfMonthQuery,
            connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                bestOfMonthMethods.value = list

            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)

            }
        }.launchIn(viewModelScope)
    }

    private fun getBestsOfWeek() {
        Log.d(TAG, "bestOfWeek: query: ${bestsOfMonthQuery}")
        // New search. Reset the state
        bests.execute(
            token = token,
            query = bestOfWeekQuery,
            connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                bestOfWeekMethods.value = list
            }

            dataState.error?.let { error ->
                //  dialogQueue.appendErrorMessage("An Error Occurred", error)

            }
        }.launchIn(viewModelScope)
    }

    private fun getBestsOfDay() {
        Log.d(TAG, "bestOfDay: query: ${bestsOfMonthQuery}")
        // New search. Reset the state
        bests.execute(
            token = token,
            query = bestOfDayQuery,
            connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                bestOfDayMethods.value = list
            }

            dataState.error?.let { error ->
                // dialogQueue.appendErrorMessage("An Error Occurred", error)

            }
        }.launchIn(viewModelScope)
    }

    private fun getFollowingsPost(){
        getHomeData.getFollowingsPost(
            token = token,
            userId = USERID,
            page= page.value,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value

        ).onEach {dataState->
            dataState.data?.let { list ->
                methods.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)
        }



    private fun nextPage() {
        if ((scrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "nextPage: triggered: ${page.value}")

            if (page.value > 1) {
                getHomeData.getFollowingsPost(
                    token = token,
                    page = page.value,
                    userId = USERID,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value
                ).onEach { dataState ->
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
    private fun appendMethods(methods: List<SewMethod>){
        val current = ArrayList(this.methods.value)
        current.addAll(methods)
        this.methods.value = current
    }
    fun onChangeScrollPosition(position: Int){
        setListScrollPosition(position = position)
    }
    private fun setListScrollPosition(position: Int){
        scrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }
    private fun incrementPage(){
        setPage(page.value + 1)
    }
    private fun setPage(page: Int){
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }
}