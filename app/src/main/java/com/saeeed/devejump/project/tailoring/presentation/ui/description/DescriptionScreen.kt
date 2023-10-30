package com.saeeed.devejump.project.tailoring.presentation.ui.description

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodView
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun DescriptionScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    sewId: Int?,
    viewModel: DescriptionViewModel,
){
    if (sewId == null){
       // TODO("Show Invalid Recipe")
    }else {
        // fire a one-off event to get the recipe from api
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(SewEvent.GetSewEvent(sewId))
        }

        val loading = viewModel.loading.value

        val sewMethod = viewModel.sewMethod.value

        val dialogQueue = viewModel.dialogQueue

        val snackBar = viewModel.customSnackBar.customSnackBarInfo
        AppTheme(
            displayProgressBar = loading,
            darkTheme = isDarkTheme,
            isNetworkAvailable = isNetworkAvailable,
            dialogQueue = dialogQueue.queue.value,
            snackBarInfo = snackBar

        ){

                Box (
                    modifier = Modifier.fillMaxSize()
                ){
                    if (loading && sewMethod == null) {
                       // LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
                    }
                    else if(!loading && sewMethod == null && onLoad){
                       // TODO("Show Invalid Recipe")
                    }
                    else {
                        sewMethod?.let {
                            SewMethodView(
                            sewMethod = it,
                                save={
                                    viewModel.saveInDataBase()
                                }

                        )
                        }
                    }
                }

        }
    }
}