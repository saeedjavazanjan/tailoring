package com.saeeed.devejump.project.tailoring.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.components.BestsRowsOfHome
import com.saeeed.devejump.project.tailoring.presentation.components.BannerViewPager
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodList
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.ui.search.SearchEvent
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
    onNavigateToBannerDestination:(String)->Unit


    ) {

    val sewMethods = viewModel.methods.value
    val page = viewModel.page.value

    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val banners=viewModel.banners
  //  val bestOfMonthMethods=viewModel.bestOfMonthMethods
   // val bestOfWeekMethods=viewModel.bestOfWeekMethods
 //   val bestOfDayMethods=viewModel.bestOfDayMethods

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    val scaffoldState= rememberScaffoldState()


    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
        LaunchedEffect(lifecycleState) {
            when (lifecycleState) {
                androidx.lifecycle.Lifecycle.State.RESUMED -> {
                  /*  if(bestOfMonthMethods.value.isEmpty() ||
                        bestOfWeekMethods.value.isEmpty() ||
                        bestOfDayMethods.value.isEmpty()
                        ){
                     //   viewModel.onTriggerEvent()
                    }*/
                }
                else -> {

                }
            }

        }
        Scaffold(
            topBar= {
                TopBar()
            }

        ) {

                Column(
                    modifier = Modifier
                     //   .verticalScroll(rememberScrollState())
                        .padding(it)
                ) {


                    BannerViewPager(
                        banners,
                        onNavigateToBannerDestination = {route->
                            try {
                                onNavigateToBannerDestination(route)

                            }catch (e:Exception){
                                dialogQueue.appendErrorMessage("Error","صفحه مورد نظر یافت نشد.")
                            }
                        },
                        context = LocalContext.current
                        )
                    Spacer(modifier = Modifier.size(50.dp))

                    if (!loading) {
                        SewMethodList(
                            loading = loading,
                            sewMethods = sewMethods,
                            onChangeScrollPosition = viewModel::onChangeScrollPosition,
                            page = page,
                            onTriggerNextPage = { viewModel.onTriggerEvent(FollowingsPostsEvent.NextPageEvent) },
                            onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,

                            )
                       /* BestsRowsOfHome(
                            onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,
                            loading = loading,
                            bestOfMonthMethods = bestOfMonthMethods,
                            bestOfWeekMethods = bestOfWeekMethods,
                            bestOfDayMethods = bestOfDayMethods,

                        )*/


                        Spacer(modifier = Modifier.size(70.dp))
                    }
                }
            }

    }
}