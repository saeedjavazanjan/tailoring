package com.saeeed.devejump.project.tailoring.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.components.BestsRowsOfHome
import com.saeeed.devejump.project.tailoring.presentation.components.HomeBannersViewPager
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class
)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: HomeViewModel,
    onNavigateToDescriptionScreen: (String) -> Unit,

    ) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val banners=viewModel.banners
    val bestOfMonthMethods=viewModel.bestOfMonthMethods



    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
    ) {
        val scrollState = rememberScrollState()

        Scaffold(
            topBar= {
                TopBar()
            }

        ) {

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(it)
                ) {


                    HomeBannersViewPager(banners)
                    Spacer(modifier = Modifier.size(200.dp))

                    BestsRowsOfHome(
                        onNavigateToDescriptionScreen = onNavigateToDescriptionScreen ,
                        loading = loading ,
                        bestOfMonthMethods = bestOfMonthMethods
                    )
                    Spacer(modifier = Modifier.size(70.dp))

                }
            }

    }
}