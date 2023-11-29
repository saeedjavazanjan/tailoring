package com.saeeed.devejump.project.tailoring.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun BannerViewPager(
    banners:MutableState<List<Banner>>,
    onNavigateToBannerDestination:(String)->Unit,
    context: Context

){
    val pagerState = rememberPagerState(pageCount = {
        banners.value.size
    })

            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                pageSpacing = 15.dp,
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )

            ){page->
                /* Card(
                     Modifier
                      .graphicsLayer {
                          // Calculate the absolute
                         offset for the current page from the
                             // scroll position. We use
                             the absolute value which allows us to mirror
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
                 )*/
                val currentBanner=banners.value[page]

                    BannerCard(
                    banner =currentBanner ,
                    onClick = {
             if (currentBanner.clickDestination.contains("http"))
                        {
                            val webIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentBanner.clickDestination))
                            context.startActivity(webIntent)
                        }else if (currentBanner.clickDestination != ""){
               val route =currentBanner.clickDestination
                   onNavigateToBannerDestination(route)
                        }

                    }
                )
                LaunchedEffect(pagerState.settledPage) {
                    delay(3000) // wait for 3 seconds.
                    // increasing the position and check the limit
            var newPosition = pagerState.settledPage + 1
         if (newPosition > banners.value.lastIndex){ newPosition = -2}
                    // scrolling to the new position.
                    pagerState.animateScrollToPage(newPosition)
                }
            }


        }




