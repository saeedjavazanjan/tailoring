package com.saeeed.devejump.project.tailoring.presentation.ui.splash

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeViewModel
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import com.saeeed.devejump.project.tailoring.utils.USERID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SplashScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: SplashViewModel,
    onNavigateToHomeScreen: (String) -> Unit,
) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
       /* LaunchedEffect(Unit){
            viewModel.getUserData(USERID)

        }*/
    }

    if (viewModel.loaded.value){
        onNavigateToHomeScreen(Screen.Home.route)
    }


}