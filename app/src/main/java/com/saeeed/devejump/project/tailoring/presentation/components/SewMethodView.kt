package com.saeeed.devejump.project.tailoring.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconToggleButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.post.PAGE_SIZE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@ExperimentalCoroutinesApi
@Composable
fun SewMethodView(
    sewMethod: SewMethod,
    bookMarkState: Boolean,
    likeState:Boolean,
    likesCount:Int,
    save:() -> Unit,
    remove:()-> Unit,
    like:() ->Unit,
    unlike:() ->Unit
) {
  //  val composableScope = rememberCoroutineScope()
  //  val snackbarController=SnackbarController(composableScope)

    val bookState= mutableStateOf(bookMarkState)
    val likeIconState= mutableStateOf(likeState)
    val likes = mutableStateOf( likesCount)
    LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    AsyncImage(
                        model = sewMethod.featuredImage,
                        contentDescription = sewMethod.title,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    //  if (bookMarkState){
                    IconToggleButton(
                        checked = bookMarkState,
                        onCheckedChange = {
                            if(bookState.value){
                                bookState.value=false
                                remove()
                            }else{
                                bookState.value=true

                                save()
                            }
                        })
                    {
                        Icon(
                            painter = painterResource(
                                if (bookState.value) R.drawable.baseline_bookmark_24
                                else R.drawable.baseline_bookmark_border_24),
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
                          //  .padding(start = 8.dp, end = 8.dp)
                    ) {
                        val(titleHolder,likeCount,likeIcon)=createRefs()
                        Text(
                            text = sewMethod.title,
                            modifier = Modifier.fillMaxWidth(0.85f)
                                .constrainAs(titleHolder){
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(likeCount.start)
                                }
                            ,
                            style = MaterialTheme.typography.bodyLarge
                        )




                        Text(
                            text = likes.value.toString(),
                            modifier = Modifier.fillMaxWidth(0.05f)
                                .constrainAs(likeCount){
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(likeIcon.start)
                                    start.linkTo(titleHolder.end)
                                }
                            ,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        IconToggleButton(
                            modifier = Modifier.fillMaxWidth(0.1f)
                                .constrainAs(likeIcon){
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                },
                            checked = likeIconState.value,
                            onCheckedChange = {
                                if(likeIconState.value){
                                    likeIconState.value=false
                                    unlike()

                                }else{
                                    likeIconState.value=true
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
                            val (avatarHolder,authorText,date) = createRefs()

                            val avatar=sewMethod.featuredImage
                          //  val (avatarHolder) = createRefs()
                            GlideImage(
                                model = avatar,
                                loading =  placeholder(R.drawable.empty_plate),
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
                            val author=sewMethod.publisher
                          //  val(authorText)=createRefs()
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
                           // val(date)=createRefs()

                        }
                val updated = sewMethod.dateUpdated
                Text(
                    text = "Updated ${updated} by ${sewMethod.publisher}",
                    color=Color.Gray,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                val ingredient = sewMethod.ingredients

                Text(
                    text = ingredient.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                    ,
                    style = MaterialTheme.typography.bodyMedium
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {

                }
               /* LazyColumn(){
                    itemsIndexed(
                        items = sewMethods
                    ) { index, sewMethod ->
                        onChangeScrollPosition(index)
                        if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                            onTriggerNextPage()
                        }
                        SewMethodCard(
                            sewMethod = sewMethod,
                            onClick = {
                                val route = Screen.SewDescription.route + "/${sewMethod.id}"
                                onNavigateToDescriptionScreen(route)
                            }
                        )
                    }

                }*/

            }
        }



}


