package com.saeeed.devejump.project.tailoring.presentation.components


import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalGlideComposeApi::class)
@ExperimentalCoroutinesApi
@Composable
fun SewMethodCard(
    post: Post,
    onClick: () -> Unit,
){
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp,
                start = 10.dp,
                end = 10.dp
            )
            .clickable(onClick = onClick)
            .width(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {

        Column() {
            post.let { method ->

                if (method.postType.equals("image")){
                    GlideImage(
                        model = method.featuredImage[0],
                        loading =  placeholder(R.drawable.empty_plate),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp),
                        contentScale = ContentScale.Crop,
                    )
                }else if(method.postType.equals("video")){
                    //with coil
                   /* val imageLoader = ImageLoader.Builder(LocalContext.current)
                        .components {
                            add(VideoFrameDecoder.Factory())
                        }
                        .build()
                    AsyncImage(
                        model = method.videoUrl,//your video here
                        imageLoader = imageLoader,
                        contentDescription = "icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp),
                        )*/

                    //with glide
                    Box( modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val requestbuilder=Glide.with(LocalView.current)
                        GlideImage(
                            model = method.videoUrl,
                            loading =  placeholder(R.drawable.empty_plate),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        ){
                            it.thumbnail(requestbuilder.asDrawable().load(Uri.parse(method.videoUrl)))
                        }
                        Icon(
                            painter = painterResource(
                               R.drawable.baseline_play_circle_outline_24),
                            contentDescription = "Radio button icon",
                            tint = Color.White
                        )
                    }

                }



            }
            post.title?.let { title ->

                Column {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start)
                            .height(30.dp)
                            .padding(5.dp)
                        ,
                        style = MaterialTheme.typography.bodySmall
                    )
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),

                    ) {
                        val avatar=post.authorAvatar
                        val (avatarHolder) = createRefs()
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
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                },
                            contentScale = ContentScale.Crop,
                        )
                        val author=post.publisher
                        val(authorText)=createRefs()
                        Text(
                            text = author,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .padding(5.dp)
                                .constrainAs(authorText) {
                                    start.linkTo(avatarHolder.end)
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                },
                            style = MaterialTheme.typography.bodySmall
                        )
                        val(likeIcon)=createRefs()

                        Icon(
                            painter = painterResource(id = R.drawable.red_heart) ,
                            tint = Color.Red,
                            contentDescription = "",
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .constrainAs(likeIcon) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                }

                        )
                        val likes = post.like.toString()
                        val(likesCount)=createRefs()

                        Text(
                            text = likes,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.End)
                                .constrainAs(likesCount) {
                                    end.linkTo(likeIcon.start)
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
               /*     Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){


                            val avatar=sewMethod.featuredImage
                            GlideImage(
                                model = avatar,
                                loading =  placeholder(R.drawable.empty_plate),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            val author=sewMethod.publisher
                            Text(
                                text = author,
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.Start),
                                style = MaterialTheme.typography.bodySmall
                            )

                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            val rank = sewMethod.rating.toString()
                            Text(
                                text = rank,
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.End),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.like) ,
                                tint = Color.Red,
                                contentDescription = "",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)

                            )
                        }

                    }*/

                }

            }
        }
    }
}