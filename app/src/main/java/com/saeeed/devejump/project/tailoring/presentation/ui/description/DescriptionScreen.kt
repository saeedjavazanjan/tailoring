package com.saeeed.devejump.project.tailoring.presentation.ui.description

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodView
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun DescriptionScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    sewId: Int?,
    viewModel: DescriptionViewModel,
    scaffoldState: ScaffoldState,
    navController: NavController
){
    if (sewId == null){
       // TODO("Show Invalid Recipe")
    }else {

        LaunchedEffect(Unit){
            viewModel.onTriggerEvent(SewEvent.GetSewEvent(sewId))

        }

        val loading = viewModel.loading.value

        val sewMethod = viewModel.sewMethod.value

        val dialogQueue = viewModel.dialogQueue
        val composableScope = rememberCoroutineScope()
        val bookMarkState=viewModel.bookMarkState.value

        AppTheme(
            displayProgressBar = loading,
            darkTheme = isDarkTheme,
            isNetworkAvailable = isNetworkAvailable,
            dialogQueue = dialogQueue.queue.value,
            scaffoldState = scaffoldState
        ){

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (loading && sewMethod == null) {
                            // LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
                        } else if (!loading && sewMethod == null ) {
                            // TODO("Show Invalid Recipe")
                        } else {
                            sewMethod?.let {
                                SewMethodView(
                                    sewMethod = it,
                                    bookMarkState=bookMarkState,
                                    save = {
                                        viewModel.saveAsBookMarkInDataBase(scaffoldState, composableScope)
                                    },
                                    remove={
                                        viewModel.removeFromBookMarkDataBase(scaffoldState,composableScope)
                                    }

                                )
                            }
                        }
                    }
                }
            }

        }
    }

}