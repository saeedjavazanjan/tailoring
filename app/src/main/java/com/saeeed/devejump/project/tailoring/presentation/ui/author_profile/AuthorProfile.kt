package com.saeeed.devejump.project.tailoring.presentation.ui.author_profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.presentation.components.PostCart
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import kotlinx.coroutines.launch
import com.google.accompanist.pager.*
import com.saeeed.devejump.project.tailoring.presentation.components.ProfileEditDialog
import com.saeeed.devejump.project.tailoring.presentation.ui.register.RegisterDialog
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter",
    "SuspiciousIndentation"
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalPagerApi::class,)
@Composable
fun UserProfileScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: AuthorProfileViewModel,
    onNavigateToDescriptionScreen: (String) -> Unit,
    onNavigateToFollowersScreen: ()->Unit,
    onNavigateToFollowingsScreen: ()->Unit,
    onNavigateToUploadPostScreen: () -> Unit
    ) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val authorToken=viewModel.authorToken
    val scaffoldState= rememberScaffoldState()
    val showDialog =  remember { mutableStateOf(false) }
    val registerDialogShow= remember { mutableStateOf(false) }
    val composableScope = rememberCoroutineScope()
    val loginState= remember { mutableStateOf(false) }
    val context= LocalContext.current
LaunchedEffect(Unit ){
    viewModel.getUserPosts()
    viewModel.getUserData()
    authorToken.value=viewModel.getTokenFromPreferencesStore()
}
    val user=viewModel.user.value

    if(registerDialogShow.value){
        RegisterDialog(
            registerShowDialog = {
                                 registerDialogShow.value=it
            },
            loginState = {
                if(it){
                    composableScope.launch {
                        authorToken.value=viewModel.getTokenFromPreferencesStore()

                    }

                }

            }
        )
    }


        if (showDialog.value) {
            ProfileEditDialog(
                showDialog = {
                    showDialog.value = it
                },
                userData = user,
                applyChanges = { imgUri, name, bio ->
                    user!!.userName = name
                    user!!.avatar = imgUri.toString()
                    user!!.bio = bio
                    viewModel.updateUserData(user, scaffoldState, composableScope,imgUri)

                }
            )
        }

        AppTheme(
            displayProgressBar = loading,
            darkTheme = isDarkTheme,
            isNetworkAvailable = isNetworkAvailable,
            dialogQueue = dialogQueue.queue.value,
            scaffoldState = scaffoldState

        ) {
            val pagerState = rememberPagerState(pageCount = 2, initialPage = 0)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                if(authorToken.value==""){
                                    registerDialogShow.value=true
                                }else{
                                    onNavigateToUploadPostScreen()

                                }

                            },
                        ) {
                            Icon(Icons.Filled.Add, "Floating action button.")
                        }
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
                            val (avatarHolder, username, bio, buttons, followers, following) = createRefs()

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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(buttons) {
                                        top.linkTo(username.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                                    shape = RoundedCornerShape(5.dp),
                                    onClick = {
                                        if(authorToken.value==""){
                                            registerDialogShow.value=true
                                        }else{
                                            showDialog.value = true
                                        }

                                    }
                                ) {
                                    Text(
                                        modifier = Modifier.padding(5.dp),
                                        text = stringResource(id = R.string.profileSetting),
                                        color = Color.DarkGray
                                    )
                                    Icon(Icons.Default.Settings, contentDescription = null)

                                }
                                Spacer(modifier = Modifier.weight(1f))

                                Button(
                                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                                    shape = RoundedCornerShape(5.dp),
                                    onClick = {
                                        if(authorToken.value==""){
                                            registerDialogShow.value=true
                                        }else{

                                        }
                                    }
                                ) {
                                    Text(
                                        modifier = Modifier.padding(5.dp),
                                        text = stringResource(id = R.string.UserTransactions),
                                        color = Color.DarkGray
                                    )
                                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                                }
                            }

                            TextButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .constrainAs(followers) {
                                        top.linkTo(buttons.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(following.start)

                                    },
                                onClick = {
                                    onNavigateToFollowersScreen()
                                }
                            ) {
                                Text(text =
                                stringResource(id = R.string.followers)+ user.followers)

                            }

                            TextButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .constrainAs(following) {
                                        top.linkTo(buttons.bottom)
                                        start.linkTo(followers.end)
                                        end.linkTo(parent.end)

                                    },
                                onClick = {
                                    onNavigateToFollowingsScreen()
                                }
                            ) {
                                Text(text = 
                                stringResource(id = R.string.followings)+ user.following)

                            }

                        }

                        Tabs(pagerState = pagerState)
                        TabsContent(
                            pagerState = pagerState,
                            viewModel = viewModel,
                            onNavigateToDescriptionScreen = onNavigateToDescriptionScreen
                        )


                    }
                }
            }


    
}
@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    viewModel: AuthorProfileViewModel,
    onNavigateToDescriptionScreen: (String) -> Unit,

    ){

    HorizontalPager(state = pagerState) {

            page ->
        when (page) {

            0 -> {
                val userPosts=viewModel.userPosts.value
                getPosts(
                    posts=userPosts,
                    onNavigateToDescriptionScreen = onNavigateToDescriptionScreen
                )

            }
            1 ->{
                 val bookMarkedPosts=viewModel.bookMarkedPosts.value
                getPosts(
                    posts=bookMarkedPosts,
                    onNavigateToDescriptionScreen = onNavigateToDescriptionScreen

                )
            }


        }
    }
}



@SuppressLint("SuspiciousIndentation")
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val tabRowItems = listOf(
        TabRowItem(
            title = stringResource(id = R.string.posts),
            icon = Icons.Default.AccountBox,
            screen = {
            }
        ) ,
        TabRowItem(
            title = stringResource(id = R.string.saved_posts),
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
fun getPosts(
    posts: List<Post>,
    onNavigateToDescriptionScreen: (String) -> Unit,


    ) {

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



