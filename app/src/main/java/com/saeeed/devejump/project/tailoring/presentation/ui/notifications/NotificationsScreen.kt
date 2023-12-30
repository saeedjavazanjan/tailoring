package com.saeeed.devejump.project.tailoring.presentation.ui.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.presentation.components.ArticleCard
import com.saeeed.devejump.project.tailoring.presentation.components.NotificationCard
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    scaffoldState: ScaffoldState
) {

    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val notifications=viewModel.notifications
    var scrollState= rememberLazyGridState()

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState
    ){


       Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ){
           if (notifications.value.isEmpty()) {

               Column(
                   modifier = Modifier
                       .padding(it).fillMaxSize(),
                   verticalArrangement = Arrangement.Center
               ) {
                   Text(
                       modifier = Modifier.align(Alignment.CenterHorizontally),
                       text = stringResource(id = R.string.there_is_not_message),
                       fontSize = 20.sp,
                       color = Color.LightGray
                   )

               }

               }else{
               LazyVerticalGrid(
                   modifier = Modifier
                       .padding(it),
                   state = scrollState,
                   columns = GridCells.Fixed(1)
               ) {

                   itemsIndexed(
                       items = notifications.value
                   ) { index, notification  ->

                       NotificationCard(
                           notification = notification,
                       )
                   }
               }

               }

           }

       }
    }