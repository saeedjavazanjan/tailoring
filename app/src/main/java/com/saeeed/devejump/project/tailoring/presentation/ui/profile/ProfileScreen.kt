package com.saeeed.devejump.project.tailoring.presentation.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.presentation.components.PostCart
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun ProfileScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: ProfileViewModel,
    onNavigateToDescriptionScreen: (String) -> Unit,
    onNavigateToFollowersScreen: ()->Unit,
    onNavigateToFollowingsScreen: ()->Unit,
    userId:Int?
) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()
    val composableScope = rememberCoroutineScope()
    val followingStatus=viewModel.followingStatus

    LaunchedEffect(Unit ){
        viewModel.getUserPosts(userId!!)
        viewModel.getUserData(userId!!)
    }
    val user=viewModel.user.value

    if( user !=null) {

        AppTheme(
            displayProgressBar = loading,
            darkTheme = isDarkTheme,
            isNetworkAvailable = isNetworkAvailable,
            dialogQueue = dialogQueue.queue.value,
            scaffoldState = scaffoldState

        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopBar()
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 12.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                        ) {
                            val (avatarHolder, username, bio, followers, following,followAndUnFollow) = createRefs()

                            GlideImage(
                                model = user?.avatar,
                                loading = placeholder(R.drawable.empty_plate),
                                contentDescription = "",
                                modifier = Modifier
                                    .height(90.dp)
                                    .width(90.dp)
                                    .padding(10.dp)
                                    .clip(CircleShape)
                                    .constrainAs(avatarHolder) {
                                        start.linkTo(parent.start)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(username.top)
                                        end.linkTo(bio.start)
                                    },
                                contentScale = ContentScale.Crop,
                            )
                            Text(
                                text = user!!.bio,
                                maxLines = 4,
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.Start)
                                    .padding(10.dp)
                                    .width(200.dp)
                                    .height(150.dp)
                                    .constrainAs(bio) {
                                        start.linkTo(avatarHolder.start)
                                        top.linkTo(avatarHolder.top)
                                        end.linkTo(parent.end)
                                    },
                                style = MaterialTheme.typography.bodyMedium

                            )

                            Text(
                                text = user.userName,
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.Start)
                                    .padding(10.dp)
                                    .constrainAs(username) {
                                        start.linkTo(parent.start)
                                        top.linkTo(avatarHolder.bottom)
                                    },
                                style = MaterialTheme.typography.bodyLarge

                            )
                            TextButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .constrainAs(followers) {
                                        top.linkTo(username.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(following.start)

                                    },
                                onClick = {
                                    onNavigateToFollowersScreen()
                                }
                            ) {
                                Text(text = stringResource(id = R.string.followers) +user.followers)

                            }

                            TextButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .constrainAs(following) {
                                        top.linkTo(username.bottom)
                                        start.linkTo(followers.end)
                                        end.linkTo(parent.end)

                                    },
                                onClick = {
                                    onNavigateToFollowingsScreen()
                                }
                            ) {
                                Text(text = "دنبال شونده گان ${user.following}")

                            }

                            OutlinedButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .constrainAs(followAndUnFollow) {
                                        top.linkTo(followers.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)

                                    },
                                onClick = {
                                },
                                border =
                                if (followingStatus.value)
                                BorderStroke(1.dp, Color.Green)
                                else
                                    BorderStroke(1.dp, Color.DarkGray)

                            ) {
                                Text(
                                    text =
                                    if (followingStatus.value)
                                    stringResource(id = R.string.followed_user)
                                        else
                                        stringResource(id = R.string.follow)
                                )

                            }

                        }
                        getThisUserPosts(posts =viewModel.userPosts.value
                            , onNavigateToDescriptionScreen = onNavigateToDescriptionScreen
                        )

                    }





                    }
                }
            }
        }

    }


@SuppressLint("UnrememberedMutableState")
@Composable
fun getThisUserPosts(
    posts: List<Post>,
    onNavigateToDescriptionScreen: (String) -> Unit,


    ) {
    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {

            itemsIndexed(
                items = posts
            ) { index, post ->
                PostCart(
                    post = post,
                    onClick = {
                        val route = Screen.SewDescription.route + "/${post.id}"
                        onNavigateToDescriptionScreen(route)
                    }
                )
            }
        }
    }


}


