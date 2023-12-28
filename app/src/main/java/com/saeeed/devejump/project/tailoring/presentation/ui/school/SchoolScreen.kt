package com.saeeed.devejump.project.tailoring.presentation.ui.school

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.Lifecycle
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.presentation.components.ArticleCard
import com.saeeed.devejump.project.tailoring.presentation.components.BannerViewPager
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodCard
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.home.FollowingsPostsEvent
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.search.PAGE_SIZE
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SchoolScreen(
    viewModel: SchoolViewModel,
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    onNavigateToBannerDestination:(String)->Unit,
    onNavigateToArticleScreen: (String)->Unit
){
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val banners=viewModel.banners

    val scaffoldState= rememberScaffoldState()
    var scrollState= rememberLazyGridState()
    val state = rememberCollapsingToolbarScaffoldState()
    val articles= viewModel.articles

    LaunchedEffect(Unit ){
        viewModel.getBanners()
        viewModel.getArticles()
    }

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {

            Scaffold(

            ) {

                Column(
                    modifier = Modifier
                        .padding(it)
                ) {

                    CollapsingToolbarScaffold(
                        modifier = Modifier,
                        state = state,
                        scrollStrategy = ScrollStrategy.EnterAlways,
                        toolbar = {
                            BannerViewPager(
                                banners,
                                onNavigateToBannerDestination = { route ->
                                    try {
                                        onNavigateToBannerDestination(route)

                                    } catch (e: Exception) {
                                        dialogQueue.appendErrorMessage(
                                            "Error",
                                            "صفحه مورد نظر یافت نشد."
                                        )
                                    }
                                },
                                context = LocalContext.current
                            )

                        }) {
                        LazyVerticalGrid(
                            state = scrollState,
                            columns = GridCells.Fixed(1)
                        ) {

                            itemsIndexed(
                                items = articles.value
                            ) { index, article ->
                             //   viewModel.onChangeScrollPosition(index)
                              /*  if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                                    viewModel.onTriggerEvent(FollowingsPostsEvent.NextPageEvent)
                                }*/
                                ArticleCard(
                                    article = article,
                                    onClick = {
                                        val route = Screen.Article.route + "/${article.id}"
                                        onNavigateToArticleScreen(route)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

}