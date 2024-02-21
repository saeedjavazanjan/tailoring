package com.saeeed.devejump.project.tailoring.presentation.ui.splash

import android.util.Log
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SplashScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: SplashViewModel,
    onNavigateToHomeScreen: (String) -> Unit,
) {
    val loading = viewModel.loading.value
    val authorToken=viewModel.authorToken
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
        LaunchedEffect(Unit){
            delay(2000)
                authorToken.value=viewModel.getTokenFromPreferencesStore()
            if (authorToken.value!=""){
                viewModel.getUserData()
            }else{
                viewModel.dataLoadedSuccessfully.value=true
            }
        }
    }

    if (viewModel.dataLoadedSuccessfully.value){
        onNavigateToHomeScreen(Screen.Home.route)
    }


}