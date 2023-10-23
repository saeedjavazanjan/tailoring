package com.saeeed.devejump.project.tailoring.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.home.GetBanners
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.sew_list.SearchSew
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
        private val getBanners: GetBanners,
        private val connectivityManager: ConnectivityManager,
        @Named("auth_token") private val token: String,
):ViewModel() {

    val banners: MutableState<List<Banner>> = mutableStateOf(ArrayList())
    val query = GET_HOME_BANNERS



    val loading = mutableStateOf(false)

    val dialogQueue = DialogQueue()

    init {
        onTriggerEvent()
    }
    fun onTriggerEvent(){
        viewModelScope.launch {
            try {
                getBanners()

            }catch (e:Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
            finally {
                Log.d(TAG, "launchJob: finally called.")
            }

        }
    }

    private fun getBanners() {
        Log.d(TAG, "getbanner: query: ${query}")

        getBanners.execute(
            token=token,
            query=query,
            connectivityManager.isNetworkAvailable.value).onEach {dataState->
            loading.value=dataState.loading
            dataState.data?.let { list ->
                banners.value = list
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }
}