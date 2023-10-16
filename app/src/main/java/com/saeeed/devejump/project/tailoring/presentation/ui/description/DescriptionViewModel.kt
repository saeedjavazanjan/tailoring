package com.saeeed.devejump.project.tailoring.presentation.ui.description

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.description.GetSewMethod
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_SEW = "sew.state.sew.key"

@ExperimentalCoroutinesApi
@HiltViewModel
class DescriptionViewModel
@Inject
constructor(
    private val getSewMethod: GetSewMethod,
    private val connectivityManager: ConnectivityManager,
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,
): ViewModel(){

    val sewMethod: MutableState<SewMethod?> = mutableStateOf(null)

    val loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    val dialogQueue=DialogQueue()

    init {
        // restore if process dies
        state.get<Int>(STATE_KEY_SEW)?.let{ sewId ->
            onTriggerEvent(SewEvent.GetSewEvent(sewId))
        }
    }

    fun onTriggerEvent(event: SewEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is SewEvent.GetSewEvent -> {
                        if(sewMethod.value == null){
                            getSewMethod(event.id)
                        }
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private fun getSewMethod(id: Int){
        getSewMethod.execute(id, token,connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { data ->
                sewMethod.value = data
                state.set(STATE_KEY_SEW, data.id)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getSew: ${error}")
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

}