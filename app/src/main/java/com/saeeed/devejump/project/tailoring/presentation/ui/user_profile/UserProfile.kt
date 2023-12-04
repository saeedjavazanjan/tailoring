package com.saeeed.devejump.project.tailoring.presentation.ui.user_profile

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material.TextButton
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.presentation.components.PostCart
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.launch
import com.google.accompanist.pager.*
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun UserProfileScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: UserProfileViewModel,
    onNavigateToDescriptionScreen: (String) -> Unit,
) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()
    val user=viewModel.user
    val posts=viewModel.userPosts.value
LaunchedEffect(Unit ){
    viewModel.getUserPosts()
}
    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
        val pagerState = rememberPagerState(pageCount =2, initialPage = 0)
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            topBar= {
                TopBar()
            }

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.size(50.dp))
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 12.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                ) {
                    val (avatarHolder, userName, profileEdit, transactions, followers, following) = createRefs()

                    GlideImage(
                        model = user.avatar,
                        loading = placeholder(R.drawable.empty_plate),
                        contentDescription = "",
                        modifier = Modifier
                            .height(70.dp)
                            .width(70.dp)
                            .fillMaxWidth(0.4f)
                            .clip(CircleShape)
                            .constrainAs(avatarHolder) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(userName.top)
                            },
                        contentScale = ContentScale.Crop,
                    )

                    Text(
                        text = user.userName,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .fillMaxWidth(0.4f)
                            .padding(10.dp)
                            .constrainAs(userName) {
                                start.linkTo(parent.start)
                                top.linkTo(avatarHolder.bottom)
                            },
                        style = MaterialTheme.typography.bodyLarge

                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(0.4f)
                            .constrainAs(profileEdit) {
                                start.linkTo(avatarHolder.end)
                                top.linkTo(avatarHolder.top)
                                bottom.linkTo(avatarHolder.bottom)

                            },
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "تنظیمات",
                            color = Color.DarkGray
                        )
                        Icon(Icons.Default.Settings, contentDescription = null)

                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .constrainAs(transactions) {
                                start.linkTo(profileEdit.end)
                                top.linkTo(avatarHolder.top)
                                bottom.linkTo(avatarHolder.bottom)

                            },
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "سبد خرید",
                            color = Color.DarkGray
                        )
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    }
                    TextButton(
                        modifier = Modifier
                            .padding(10.dp)
                            .constrainAs(followers) {
                                top.linkTo(userName.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(following.start)

                            },
                        onClick = {
                            /*TODO*/
                        }
                    ) {
                        Text(text = "دنبال کننده گان ${user.followers.size}")

                    }

                    TextButton(
                        modifier = Modifier
                            .padding(10.dp)
                            .constrainAs(following) {
                                top.linkTo(userName.bottom)
                                start.linkTo(followers.end)
                                end.linkTo(parent.end)

                            },
                        onClick = {
                            /*TODO*/
                        }
                    ) {
                        Text(text = "دنبال شونده گان ${user.following.size}")

                    }

                }

                Tabs(pagerState = pagerState)
                TabsContent(pagerState = pagerState, posts = posts)


            }
        }
    }


}
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState,posts:List<SewMethod>) {

    HorizontalPager(state = pagerState) {

            page ->
        when (page) {

            0 -> getPosts(posts=posts)
            1 -> getPosts(posts=posts)


        }
    }
}



@SuppressLint("SuspiciousIndentation")
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val tabRowItems = listOf(
        TabRowItem(
            title = "پست ها",
            icon = Icons.Default.AccountBox,
            screen = {
            }
        ) ,
        TabRowItem(
            title = "ذخیره ها",
            icon = ImageVector.vectorResource(R.drawable.baseline_bookmark_border_24),
            screen = {

            }
        ) ,
    )

    val scope = rememberCoroutineScope()

      TabRow(
                    modifier=Modifier.fillMaxWidth(),
                    backgroundColor=Color.White,
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                ) {
                    tabRowItems.forEachIndexed { index, item ->
                        Tab(
                            selectedContentColor= Color.DarkGray,
                            unselectedContentColor= Color.LightGray,
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch {
                                pagerState.animateScrollToPage(index)



                            }

                                      },
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = "")
                            },
                            text = {
                                Text(
                                    text = item.title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                        )
                    }


                    // Will be added later
                }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun getPosts(posts: List<SewMethod>) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {

            itemsIndexed(
                items = posts
            ) { index, post ->
                PostCart(post = post)
            }
        }
    }
}



