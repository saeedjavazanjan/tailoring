package com.saeeed.devejump.project.tailoring.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.utils.USERID
import com.saeeed.devejump.project.tailoring.utils.USER_AVATAR
import com.saeeed.devejump.project.tailoring.utils.USER_NAME
import kotlinx.coroutines.ExperimentalCoroutinesApi


@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState", "MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalMaterial3Api::class
)
@ExperimentalCoroutinesApi
@Composable
fun SewMethodView(
    loading:Boolean,
    sewMethod: SewMethod,
    bookMarkState: Boolean,
    likeState:Boolean,
    likesCount:Int,
    comments:MutableList<Comment>,
    save:() -> Unit,
    removeBookMark:()-> Unit,
    removeComment: (comment:Comment) -> Unit,
    like:() ->Unit,
    unlike:() ->Unit,
    Insertcomment:(comment:Comment) -> Unit,
    editComment:(comment:Comment)-> Unit,
    report:(comment:Comment) -> Unit,
    sellItem: () -> Unit,
    commentSendLoading:Boolean
) {
    val listOfComments= mutableStateOf(comments)


    val bookState= mutableStateOf(bookMarkState)
    val likeIconState= mutableStateOf(likeState)
    val likes = mutableStateOf( likesCount)
    val openDialog = remember { mutableStateOf(false) }

    var scrollState= rememberLazyListState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var query= remember {
        mutableStateOf("")
    }
    var commentState= remember { mutableStateOf("firstComment") }


        var onEditComment  = remember {
            mutableStateOf( Comment(0,"","","",0,"",sewMethod.id))
        }

    var onReportComment= remember {
        mutableStateOf( Comment(0,"","","",0,"",sewMethod.id))
    }


    LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp),
                        contentAlignment = Alignment.TopEnd,
                    ) {
                        if (sewMethod.postType.equals("video")) {
                            val context = LocalContext.current
                            VideoPlayer(
                                videoUrl = sewMethod.videoUrl,
                                context = context
                            )

                        } else {
                            AsyncImage(
                                model = sewMethod.featuredImage,
                                contentDescription = sewMethod.title,
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }

                        //  if (bookMarkState){
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
                                text = sewMethod.title,
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
                                    contentDescription = "Radio button icon",
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

                        val avatar = sewMethod.featuredImage
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
                        val author = sewMethod.publisher
                        Text(
                            text = author,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .padding(10.dp)
                                .constrainAs(authorText) {
                                    start.linkTo(avatarHolder.end)
                                    bottom.linkTo(avatarHolder.bottom)
                                    top.linkTo(avatarHolder.top)
                                },
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                    val updated = sewMethod.dateUpdated
                    Text(
                        text = "Updated ${updated} by ${sewMethod.publisher}",
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 10.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    val description = sewMethod.description

                    Text(
                        text = description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 20.dp, end = 10.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {

                    }

                    Text(
                        modifier = Modifier.padding(10.dp),

                        text = "نظرات"
                    )
                    CommentsList(
                        comments = listOfComments.value,
                        showReportDialog = {
                            openDialog.value = true
                            onReportComment.value=it

                        },
                        editComment={
                            focusRequester.requestFocus()
                            commentState.value = "editComment"
                            query.value=it.comment
                            onEditComment.value=it

                        },
                        remove = {
                            removeComment(it)


                        },

                        loading=loading
                        )

                    if(openDialog.value){
                        ReportAlertDialog(
                            ok={
                                openDialog.value = false

                                report(onReportComment.value)

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
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        )
        if (scrollState.canScrollBackward) {
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
                        if (!query.value.equals("")) {
                            if (commentState.value.equals("firstComment")) {
                                Insertcomment(
                                    Comment(
                                        sewMethod.comments.size,
                                        query.value,
                                        USER_AVATAR,
                                        USER_NAME,
                                        USERID,
                                        "یکم آبان",
                                        postId = sewMethod.id
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
                            focusManager.clearFocus()                        }
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

        }
    }
}


@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentsList(
    comments:MutableList<Comment>,
    showReportDialog:(comment:Comment) -> Unit,
    editComment: (comment:Comment) -> Unit,
    remove: (comment:Comment) -> Unit,
    loading: Boolean
) {



    if (!loading ) {
        FlowColumn {
            comments.forEach { comment->
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


