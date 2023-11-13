package com.saeeed.devejump.project.tailoring.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.home.Bests
import com.saeeed.devejump.project.tailoring.interactor.home.GetBanners
import com.saeeed.devejump.project.tailoring.utils.BEST_OF_DAY
import com.saeeed.devejump.project.tailoring.utils.BEST_OF_MONTH
import com.saeeed.devejump.project.tailoring.utils.BEST_OF_WEEK
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.GET_HOME_BANNERS
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val bests: Bests,
    private val getBanners: GetBanners,
    private val connectivityManager: ConnectivityManager,
    @Named("auth_token") private val token: String,
) : ViewModel() {

    val banners: MutableState<List<Banner>> = mutableStateOf(ArrayList())
    val bannersQuery = GET_HOME_BANNERS
    val bestsOfMonthQuery = BEST_OF_MONTH
    val bestOfWeekQuery = BEST_OF_WEEK
    val bestOfDayQuery = BEST_OF_DAY

    val bestOfMonthMethods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())
    val bestOfWeekMethods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())
    val bestOfDayMethods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())


    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()

    init {
        onTriggerEvent()
    }

    fun onTriggerEvent() {
        viewModelScope.launch {
            try {
                getBanners()
              //  getBestsOfMonth()
              //  getBestsOfWeek()
               // getBestsOfDay()

            } catch (e: Exception) {
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            } finally {
                Log.d(TAG, "launchJob: finally called.")
            }

        }
    }

    private fun getBanners() {
        Log.d(TAG, "getbanner: query: ${bannersQuery}")

        getBanners.execute(
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
}