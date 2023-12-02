package com.saeeed.devejump.project.tailoring.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.components.BestsRowsOfHome
import com.saeeed.devejump.project.tailoring.presentation.components.BannerViewPager
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodCard
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodList
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.search.PAGE_SIZE
import com.saeeed.devejump.project.tailoring.presentation.ui.search.SearchEvent
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

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
    var scrollState= rememberLazyGridState()
    val state = rememberCollapsingToolbarScaffoldState()


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

                    var visible by remember { mutableStateOf(true) }


                  /*  if (!scrollState.canScrollBackward && scrollState.canScrollForward){

                        visible=true

                    }else if (scrollState.canScrollBackward){
                        visible=false
                    }*/
               //     AnimatedVisibility(visible) {
                    CollapsingToolbarScaffold(
                        modifier = Modifier,
                        state = state,
                        scrollStrategy = ScrollStrategy.EnterAlways,
                        toolbar = {
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

                        }){
                   //     if (!loading) {
                            LazyVerticalGrid(
                                state=scrollState,
                                columns = GridCells.Fixed(2) ) {

                                itemsIndexed(
                                    items = sewMethods
                                ) { index, sewMethod ->
                                    viewModel.onChangeScrollPosition(index)
                                    if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                                        viewModel.onTriggerEvent(FollowingsPostsEvent.NextPageEvent)
                                    }
                                    SewMethodCard(
                                        sewMethod = sewMethod,
                                        onClick = {
                                            val route = Screen.SewDescription.route + "/${sewMethod.id}"
                                            onNavigateToDescriptionScreen(route)
                                        }
                                    )
                                }
                            }



                          /*  SewMethodList(
                                loading = loading,
                                scrollState=scrollState,
                                sewMethods = sewMethods,
                                onChangeScrollPosition = viewModel::onChangeScrollPosition,
                                page = page,
                                onTriggerNextPage = { viewModel.onTriggerEvent(FollowingsPostsEvent.NextPageEvent) },
                                onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,

                                )*/
                            /* BestsRowsOfHome(
                                 onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,
                                 loading = loading,
                                 bestOfMonthMethods = bestOfMonthMethods,
                                 bestOfWeekMethods = bestOfWeekMethods,
                                 bestOfDayMethods = bestOfDayMethods,

                             )*/


                          //  Spacer(modifier = Modifier.size(70.dp))
                       // }
                    }

                  //  }




                }
            }

    }
}