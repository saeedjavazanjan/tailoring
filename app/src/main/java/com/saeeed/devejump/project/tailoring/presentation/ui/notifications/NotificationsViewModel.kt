package com.saeeed.devejump.project.tailoring.presentation.ui.notifications

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Notification
import com.saeeed.devejump.project.tailoring.interactor.notifications.GetNotifications
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val getNotifications: GetNotifications,
    @Named("auth_token") private val token: String,):ViewModel(

){
    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val notifications:MutableState<List<Notification>> = mutableStateOf(ArrayList())

    init {
        getNotifications()
    }

    private fun getNotifications() {
        getNotifications.getFromServer(
            token = token,
            connectivityManager.isNetworkAvailable.value
        ).onEach {dataState->
            loading.value=dataState.loading

            dataState.data?.let {
                notifications.value=it

            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("خطا",it)
            }


        }.catch {
            dialogQueue.appendErrorMessage("خطا",it.message.toString())

        }.launchIn(viewModelScope)
    }


}