package com.saeeed.devejump.project.tailoring.presentation.ui.description

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.description.GetSewMethod
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_RECIPE = "recipe.state.recipe.key"

@ExperimentalCoroutinesApi
@HiltViewModel
class DescriptionViewModel
@Inject
constructor(
    private val getSewMethod: GetSewMethod,
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,
): ViewModel(){

    val recipe: MutableState<SewMethod?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    init {
        // restore if process dies
        state.get<Int>(STATE_KEY_RECIPE)?.let{ recipeId ->
         //   onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

   /* fun onTriggerEvent(event: RecipeEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is GetRecipeEvent -> {
                        if(recipe.value == null){
                            getRecipe(event.id)
                        }
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }*/

    private suspend fun getSewMethod(id: Int){
        getSewMethod.execute(id, token,true).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { data ->
                recipe.value = data
                state.set(STATE_KEY_RECIPE, data.id)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getRecipe: ${error}")
                //dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

}