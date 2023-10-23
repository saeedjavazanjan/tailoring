package com.saeeed.devejump.project.tailoring.presentation.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.presentation.components.BannerCard
import com.saeeed.devejump.project.tailoring.presentation.components.SearchAppBar
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodCard
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.list.SewListEvent
import com.saeeed.devejump.project.tailoring.presentation.ui.list.getAllCategories
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class
)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: HomeViewModel,

    ) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val banners=viewModel.banners
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
            Box(

                modifier = Modifier
                    .scrollable(state = scrollState, orientation = Orientation.Vertical)
            ) {
                Column {
                    
                    val pagerState = rememberPagerState(pageCount = {
                        banners.value.size
                    })
                    HorizontalPager(state = pagerState){page->
                        Card(
                            Modifier
                                .graphicsLayer {
                                    // Calculate the absolute offset for the current page from the
                                    // scroll position. We use the absolute value which allows us to mirror
                                    // any effects for both directions
                                    val pageOffset = (
                                            (pagerState.currentPage - page) + pagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue

                                    // We animate the alpha, between 50% and 100%
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                }
                        ) {
                            BannerCard(
                                banner = banners.value[page],
                                onClick = {
                                    /*    val route = Screen.SewDescription.route + "/${sewMethod.id}"
                                        onNavigateToDescriptionScreen(route)*/
                                }
                            )

                            // Card content
                        }
                        LaunchedEffect(pagerState.currentPage) {
                            delay(3000) // wait for 3 seconds.
                            // increasing the position and check the limit
                            var newPosition = pagerState.currentPage + 1
                            if (newPosition > banners.value.lastIndex) newPosition = 0
                            // scrolling to the new position.
                            pagerState.animateScrollToPage(newPosition)
                        }
                    }


                }



            }
        }
    }
}