package com.saeeed.devejump.project.tailoring.presentation.ui.description

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
import com.saeeed.devejump.project.tailoring.presentation.components.CommentCard
import com.saeeed.devejump.project.tailoring.presentation.components.ReportAlertDialog
import com.saeeed.devejump.project.tailoring.presentation.components.VideoPlayer
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import com.saeeed.devejump.project.tailoring.utils.TimeConvertor
import com.saeeed.devejump.project.tailoring.utils.USERID
import com.saeeed.devejump.project.tailoring.utils.USER_AVATAR
import com.saeeed.devejump.project.tailoring.utils.USER_NAME
import kotlinx.coroutines.ExperimentalCoroutinesApi


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation",
    "CoroutineCreationDuringComposition", "UnrememberedMutableState"
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun DescriptionScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    sewId: Int?,
    viewModel: DescriptionViewModel,
    scaffoldState: ScaffoldState,
    onNavigateToProfile: (String)->Unit,
    onNavigateToProductDetailScreen: () -> Unit
){
    if (sewId == null){
       // TODO("Show Invalid Recipe")
    }else
    {

        LaunchedEffect(Unit){
            viewModel.onTriggerEvent(SewEvent.GetSewEvent(sewId))
        }
        val comments=viewModel.comments.value
        val product=viewModel.product.value
        val loading = viewModel.loading.value
        val productLoading=viewModel.productLoading.value
        val commentSendLoading=viewModel.commentSendLoading.value
        val sewMethod = viewModel.post.value
        val dialogQueue = viewModel.dialogQueue
        val composableScope = rememberCoroutineScope()
        val bookMarkState=viewModel.bookMarkState.value
        val likeState=viewModel.liKeState.value
        val likesCount =viewModel.likeCount.value
        AppTheme(
            displayProgressBar = loading,
            darkTheme = isDarkTheme,
            isNetworkAvailable = isNetworkAvailable,
            dialogQueue = dialogQueue.queue.value,
            scaffoldState = scaffoldState
        ){




                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (loading && sewMethod == null) {
                            // LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
                        } else if (!loading && sewMethod == null ) {
                            // TODO("Show Invalid Recipe")
                        } else {
                            sewMethod?.let {post->

                                val openDialog = remember { mutableStateOf(false) }
                                val removeState = remember { mutableStateOf(false) }
                                val editState = remember { mutableStateOf(false) }

                                var scrollState= rememberLazyListState()

                                val focusRequester = remember { FocusRequester() }
                                var query= remember {
                                    mutableStateOf("")
                                }
                                var commentState= remember { mutableStateOf("firstComment") }


                                var onEditComment  = remember {
                                    mutableStateOf( Comment(0,"","","",0,System.currentTimeMillis(),sewMethod!!.id))
                                }

                                var onReportComment= remember {
                                    mutableStateOf( Comment(0,"","","",0,System.currentTimeMillis(),sewMethod!!.id))
                                }

                                var onRemoveComment= remember {
                                    mutableStateOf( Comment(0,"","","",0,System.currentTimeMillis(),sewMethod!!.id))
                                }

                                if (removeState.value){
                                      viewModel.removeComment(onRemoveComment.value ,scaffoldState,composableScope)
                                    removeState.value=false
                                }

                                if(editState.value){
                                    viewModel.editComment(comment = onEditComment.value)

                                    editState.value=false
                                }

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
                                           imageAndVideoHolder(
                                               post = post,
                                               bookMarkState = bookMarkState,
                                               save = {
                                                   viewModel.bookMark(scaffoldState, composableScope)

                                               },
                                               removeBookMark = {
                                                   viewModel.removeFromBookMarkDataBase(scaffoldState,composableScope)


                                               }
                                           )
                                           detail(
                                               post = post,
                                               likesCount = likesCount,
                                               likeState = likeState,
                                               like = {
                                                   viewModel.likePost()

                                               },
                                               unlike = {
                                                   viewModel.unLikePost()

                                               },
                                               onNavigateToProfile={route->
                                                   onNavigateToProfile(route)
                                               }

                                           )
                                           if(post.haveProduct==1) {
                                               if(productLoading){
                                                   DotsFlashing()
                                               }else product?.let {
                                                   fileOrProductOfPost(
                                                       product = product,
                                                       onNavigateTpProductDetailScreen = {

                                                           onNavigateToProductDetailScreen()
                                                       }
                                                   )
                                               }
                                           }
                                           Spacer(modifier = Modifier.size(100.dp))
                                           Text(
                                               modifier = Modifier.padding(10.dp),

                                               text = "نظرات"
                                           )


                                           CommentsList(
                                               comments = comments,
                                               showReportDialog = { comment->
                                                   openDialog.value = true
                                                   onReportComment.value=comment

                                               },
                                               editComment={ comment->
                                                   focusRequester.requestFocus()
                                                   commentState.value = "editComment"
                                                   query.value=comment.comment
                                                   onEditComment.value=comment

                                               },
                                               remove = {comment->
                                                 //  viewModel.removeComment(comment ,sewId,scaffoldState,composableScope)
                                               onRemoveComment.value=comment
                                                   removeState.value=true


                                               },

                                               loading=loading
                                           )

                                           if(openDialog.value){
                                               ReportAlertDialog(
                                                   ok={
                                                       openDialog.value = false

                                                       viewModel.reportCommenet(onReportComment.value,sewId)

                                                   },
                                                   cancle = {
                                                       openDialog.value = false

                                                   }
                                               )



                                           }

                                           Spacer(modifier = Modifier.size(80.dp))
                                       }
                                   }
                                }

                                commentTextField(
                                    post = post,
                                    scrollState = scrollState ,
                                    query = query ,
                                    commentState = commentState,
                                    editComment ={comment->
                                       // viewModel.editComment(comment = comment, postId = sewId)
                                       // onEditComment.value=comment
                                        editState.value=true
                                    } ,
                                    insertComment ={ comment->
                                        viewModel.commentOnPost(comment = comment)

                                    } ,

                                    commentSendLoading =commentSendLoading ,
                                    onEditComment =onEditComment ,
                                    focusRequester =focusRequester,
                                    comments = comments
                                )



/*

                                    SewMethodView(
                                        sewMethod = it,
                                        bookMarkState =bookMarkState,
                                        likeState =likeState,
                                        likesCount = likesCount,
                                        comments =comments.value,
                                        loading=loading,
                                        commentSendLoading=commentSendLoading,
                                        save = {
                                            viewModel.bookMark(scaffoldState, composableScope)
                                        },
                                        removeBookMark ={
                                            viewModel.removeFromBookMarkDataBase(scaffoldState,composableScope)
                                        },
                                        like = {
                                            viewModel.likePost()
                                        },
                                        unlike = {
                                            viewModel.unLikePost()
                                        },
                                        Insertcomment = {
                                         viewModel.commentOnPost(comment = it, postId = sewId)

                                        },
                                        editComment = {
                                                     viewModel.editComment(it,sewId)
                                        },
                                        report = {
                                                 viewModel.reportCommenet(it,sewId)

                                        },
                                        removeComment = {
                                           viewModel.removeComment(it,sewId,scaffoldState,composableScope)
                                        },

                                        sellItem = {

                                        }

                                    )
*/


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
fun imageAndVideoHolder(
    post:Post,
    bookMarkState:Boolean,
    save:() -> Unit,
    removeBookMark:()-> Unit,

    ){
    val bookState= mutableStateOf(bookMarkState)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp),
        contentAlignment = Alignment.TopEnd,
    ) {
        if (post.postType.equals("video")) {
            val context = LocalContext.current
            VideoPlayer(
                videoUrl = post.videoUrl,
                context = context
            )

        } else {
            val pagerState = rememberPagerState(pageCount = {
                post.featuredImage.size
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
        model = post.featuredImage[page],
        contentDescription = post.title,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Inside,
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

        IconToggleButton(
            checked = bookMarkState,
            onCheckedChange = {
                if (bookState.value) {
                    bookState.value = false
                    removeBookMark()
                } else {
                    bookState.value = true

                    save()
                }
            })
        {
            Icon(
                painter = painterResource(
                    if (bookState.value) R.drawable.baseline_bookmark_24
                    else R.drawable.baseline_bookmark_border_24
                ),
                contentDescription = "Radio button icon",
                tint = Color(
                    0xFF9B51E0
                )
            )
        }


    }


}
@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun detail(
    post:Post,
    likesCount:Int,
    likeState:Boolean,
    like:() ->Unit,
    unlike:() ->Unit,
    onNavigateToProfile: (String)->Unit

) {
    val likes = mutableStateOf( likesCount)
    val likeIconState= mutableStateOf(likeState)
    val timeConverter=TimeConvertor

    val expanded= remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight()
            .padding(top = 8.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            val (titleHolder, likeCount, likeIcon) = createRefs()
            Text(
                text = post.title,
                modifier = Modifier
                    .constrainAs(titleHolder) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                style = MaterialTheme.typography.bodyLarge
            )




            Text(
                text = likes.value.toString(),
                modifier = Modifier
                    .constrainAs(likeCount) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(likeIcon.start)
                    },
                style = MaterialTheme.typography.bodyMedium
            )

            IconToggleButton(
                modifier = Modifier
                    .constrainAs(likeIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                checked = likeIconState.value,
                onCheckedChange = {
                    if (likeIconState.value) {
                        likeIconState.value = false
                        //  likes.value--
                        unlike()

                    } else {
                        likeIconState.value = true
                        //  likes.value++
                        like()
                    }
                })
            {
                Icon(
                    painter = painterResource(R.drawable.gray_heart),
                    contentDescription = "",
                    tint = if (likeIconState.value) Color.Red else Color.LightGray
                )
            }
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight()
            .padding(top = 12.dp, start = 8.dp, end = 8.dp),
    ) {
        val (avatarHolder, authorText) = createRefs()

        val avatar = post.authorAvatar
        GlideImage(
            model = avatar,
            loading = placeholder(R.drawable.empty_plate),
            contentDescription = "",
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .clip(CircleShape)
                .constrainAs(avatarHolder) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            contentScale = ContentScale.Crop,
        )
        val author = post.publisher

        TextButton(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start)
                .padding(10.dp)
                .constrainAs(authorText) {
                    start.linkTo(avatarHolder.end)
                    bottom.linkTo(avatarHolder.bottom)
                    top.linkTo(avatarHolder.top)
                },
            onClick = {
                val route = Screen.Profile.route + "/${post.authorId}"
                onNavigateToProfile(route)

            }
        ){
            Text(
                text = author,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
    val date = post.longDataAdded
    val dateText=""//timeConverter.timeAndTimeUnitCalculator(date)
    Text(
        text = dateText,//"Updated $diffrence by ${post.publisher}",
        color = Color.Gray,
        modifier = Modifier
            .padding(start = 10.dp),
        style = MaterialTheme.typography.bodySmall
    )
    if(post.description.length>100){
        val description = remember {
            mutableStateOf(post.description.substring(0,90))
        }
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                    append(description.value)
                }
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append(
                        text =
                        if (!expanded.value)
                            "\n...بیشتر"
                        else
                            "\n...کمتر"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                .clickable(
                    onClick = {
                        if (!expanded.value) {
                            expanded.value = true
                            description.value = post.description
                        } else {
                            expanded.value = false
                            description.value = post.description.substring(0, 90)
                        }

                    }
                ),
        )
    }else{
        val description=post.description
         Text(

                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
    }



}




@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun fileOrProductOfPost(
    product:Product,
onNavigateTpProductDetailScreen:()->Unit
){
    Card(
        modifier=Modifier.padding(20.dp),
        elevation= CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                GlideImage(
                    model = product.images[0],
                    contentDescription = "",
                    loading=placeholder(R.drawable.empty_plate),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    modifier=Modifier.align(Alignment.Bottom),
                    text =product.name
                )
                Spacer(modifier = Modifier.weight(1f))

            }
            Text(
                modifier=Modifier.padding(10.dp),
                text =product.description
            )

            Text(
                modifier=Modifier.padding(10.dp),

                text ="قیمت:  ${product.price} تومان "
            )
               TextButton(
                   modifier=Modifier
                       .align(Alignment.End),
                   onClick = {
                       onNavigateTpProductDetailScreen()

                   }) {
                   Text(
                       modifier=Modifier.padding(10.dp),
                       text = stringResource(id =R.string.more),
                       color= Color.Blue
                   )
               }

        }




    }


}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentsList(
    comments:MutableList<Comment>?,
    showReportDialog:(comment:Comment) -> Unit,
    editComment: (comment:Comment) -> Unit,
    remove: (comment:Comment) -> Unit,
    loading: Boolean
) {

    val listOfComments= mutableStateOf(comments)

    if (!loading ) {
        FlowColumn {
            listOfComments.value!!.forEach { comment->
                CommentCard(
                    comment = comment,
                    edit = {
                        editComment(
                            comment
                        )
                    },
                    report = {
                        showReportDialog(comment)
                    },
                    removeComment = {
                        remove(comment)
                    }

                )


            }
        }


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun commentTextField(
    post:Post,
    comments: MutableList<Comment>,
    scrollState:LazyListState,
    query:MutableState<String>,
    commentState:MutableState<String>,
    editComment:(comment:Comment)-> Unit,
    insertComment:(comment:Comment) -> Unit,
    commentSendLoading:Boolean,
    onEditComment:MutableState<Comment>,
    focusRequester:FocusRequester
){
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        )
      //  if (scrollState.canScrollBackward) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),

                ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 3.dp)
                        .fillMaxWidth(0.1f),
                    onClick = {
                        if (!query.equals("")) {
                            if (commentState.value.equals("firstComment")) {
                                var id=0

                                if (comments.size !=0){
                                    id=comments.last().id+1
                                }
                                insertComment(

                                    Comment(
                                        id,
                                        query.value,
                                        USER_AVATAR,
                                        USER_NAME,
                                        USERID,
                                        System.currentTimeMillis(),
                                        postId = post.id
                                    )
                                )
                            }
                            else if (commentState.value.equals("editComment")) {
                                onEditComment.let {
                                    it.value.comment = query.value
                                    editComment(it.value)
                                    commentState.value = "firstComment"

                                }
                            }
                        }

                        focusManager.clearFocus()
                        query.value=""

                    }

                ){
                    if(!commentSendLoading){
                        Icon( Icons.Filled.Send,
                            contentDescription = null)
                    }else{
                        Icon( Icons.Filled.Done,
                            contentDescription = null)
                    }

                    // painter = painterResource(R.drawable.ic_baseline_print_24),
                }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .fillMaxWidth(0.9f)
                        .focusRequester(focusRequester),
                    value = query.value,
                    shape = MaterialTheme.shapes.medium,
                    trailingIcon = {
                            Icon(Icons.Default.Clear,
                                contentDescription = "clear text",
                                modifier = Modifier
                                    .clickable {
                                        query.value = ""
                                        focusManager.clearFocus()
                                        if (commentState.value.equals("editComment"))
                                            commentState.value="firstComment"
                                    }
                            )


                    },
                    onValueChange = {
                        query.value=it
                    },
                    label = {
                        if (commentState.value.equals("firstComment"))
                            Text(text = "در باره پست نظر بدهید")

                        else if(commentState.value.equals("editComment"))
                            Text(text = "ویرایش نظر")

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            //  comment()
                            focusManager.clearFocus()
                        }
                    ),

                    colors = TextFieldDefaults.textFieldColors(

                        textColor = Color.DarkGray,
                        placeholderColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )

                    )

            }

     //   }
    }
}




