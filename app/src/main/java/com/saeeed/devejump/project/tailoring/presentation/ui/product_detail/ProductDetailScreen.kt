package com.saeeed.devejump.project.tailoring.presentation.ui.product_detail

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.components.progress_bar.DotsFlashing
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.presentation.components.ReportAlertDialog
import com.saeeed.devejump.project.tailoring.presentation.components.VideoPlayer
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.CommentsList
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.description.SewEvent
import com.saeeed.devejump.project.tailoring.presentation.ui.description.commentTextField
import com.saeeed.devejump.project.tailoring.presentation.ui.description.fileOrProductOfPost
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun ProductDetailScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    scaffoldState: ScaffoldState,
    viewModel:DescriptionViewModel
) {


        val product=viewModel.product.value
        val loading = viewModel.loading.value
        val dialogQueue = viewModel.dialogQueue
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
                        if (loading && product == null) {
                            // LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
                        } else if (!loading && product == null ) {
                            // TODO("Show Invalid Recipe")
                        } else {
                            product?.let {prod->

                                var scrollState= rememberLazyListState()

                                LazyColumn(
                                    state = scrollState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ){
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            imageHolder(
                                                product = prod,
                                            )
                                            detail(
                                                product = prod,
                                            )

                                            Spacer(modifier = Modifier.size(100.dp))



                                            Spacer(modifier = Modifier.size(80.dp))
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }

        }
    }



@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun imageHolder(
    product: Product,
    ){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp),
        contentAlignment = Alignment.TopEnd,
    ) {

            val pagerState = rememberPagerState(pageCount = {
                product.images.size
            })
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSpacing = 15.dp,
                contentPadding = PaddingValues(
                    vertical = 5.dp
                )

            ) { page ->
                AsyncImage(
                    model = product.images[page],
                    contentDescription =null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

            }
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(color, CircleShape)
                            .size(10.dp)
                    )
                }

            }
        }


    }



@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun detail(
    product: Product,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight()
            .padding(top = 8.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
    ) {

        Text(
            text = product.name,
            modifier = Modifier.padding(10.dp),
            fontSize = 20.sp
            )

        Spacer(modifier = Modifier.size(50.dp))
        val description = product.description
        Text(
            text = description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 20.dp, end = 10.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.size(30.dp))
        if (product.typeOfProduct.equals("محصول فیزیکی")) {
            Text(
                text =
                if (product.supply.toInt() > 0)
                    stringResource(id = R.string.there_is_in_warehouse)
                else
                    stringResource(id = R.string.there_is_not_in_warehouse),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                fontSize = 20.sp,
                color = Color.LightGray
            )
        }
        Text(
            text = "قیمت : ${product.price} تومان ",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 20.dp, end = 10.dp),
            fontSize = 16.sp,
            color = Color.DarkGray
        )
        if (product.typeOfProduct.equals("محصول فیزیکی")) {
            AnimatedCounter(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.End)
                ,

            )

        }
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 100.dp),
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(5.dp),
            colors =
            if (product.supply.toInt() > 0)
                ButtonDefaults.buttonColors(Color.Green)
            else
                ButtonDefaults.buttonColors(Color.LightGray),
        ) {
            Text(
                text =
                if (product.supply.toInt() > 0)
                    stringResource(id = R.string.buy)
                else
                    stringResource(id = R.string.there_is_not_in_warehouse),
                fontSize = 20.sp
            )
        }


    }


}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val count= remember() {
        mutableStateOf(1)
    }
    val oldCount = remember {
        mutableIntStateOf(count.value)
    }
Row(modifier = modifier) {
    Button(
        modifier=Modifier.padding(10.dp),
        onClick = { count.value++ }) {
        Text(text = "+")
    }
    Row(
        modifier=Modifier.align(Alignment.CenterVertically),
        horizontalArrangement = Arrangement.Start
    ) {
        val countString = count.value.toString()
        val oldCountString = oldCount.intValue.toString()
        for(i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if(oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } with slideOutVertically { -it }
                }, label = ""
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false
                )
            }

        }
    }
    Button(
        modifier=Modifier.padding(10.dp),
        onClick = { count.value-- }) {
        Text(text = "-")
    }
}



}



