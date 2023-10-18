package com.saeeed.devejump.project.tailoring.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.sew_list.SearchSew
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val searchSew: SearchSew,
        private val connectivityManager: ConnectivityManager,
        @Named("auth_token") private val token: String,
):ViewModel() {

    val banners: MutableState<List<Banner>> = mutableStateOf(ArrayList())
    val query = mutableStateOf("")



    val loading = mutableStateOf(false)

    val dialogQueue = DialogQueue()

    fun onTriggerEvent(){
        viewModelScope.launch {
            try {
                getBanners()

            }catch (e:Exception){

            }
        }
    }

    private fun getBanners() {
        TODO("Not yet implemented")
    }
}