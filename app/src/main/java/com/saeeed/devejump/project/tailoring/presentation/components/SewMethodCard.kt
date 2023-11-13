package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalGlideComposeApi::class)
@ExperimentalCoroutinesApi
@Composable
fun SewMethodCard(
    sewMethod: SewMethod,
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
            .clickable(onClick = onClick).
        width(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {

        Column() {
            sewMethod.let { method ->

                if (
                    method.featuredImage.takeLast(3)=="png" ||
                    method.featuredImage.takeLast(3)=="jpg"
                ){
                    GlideImage(
                        model = method.featuredImage,
                        loading =  placeholder(R.drawable.empty_plate),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp),
                        contentScale = ContentScale.Crop,
                    )
                }else{
                    GlideImage(
                        model = method.videoUrl,
                       // loading =  placeholder(R.drawable.empty_plate),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp),
                        contentScale = ContentScale.Crop,
                    )
                }



            }
            sewMethod.title?.let { title ->

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
                        modifier = Modifier.fillMaxWidth(1f)
                            .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),

                    ) {
                        val avatar=sewMethod.featuredImage
                        val (avatarHolder) = createRefs()
                        GlideImage(
                            model = avatar,
                            loading =  placeholder(R.drawable.empty_plate),
                            contentDescription = "",
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clip(CircleShape)
                                .constrainAs(avatarHolder){
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                },
                            contentScale = ContentScale.Crop,
                        )
                        val author=sewMethod.publisher
                        val(authorText)=createRefs()
                        Text(
                            text = author,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .padding(5.dp)
                                .constrainAs(authorText){
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
                                .constrainAs(likeIcon){
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(parent.top)
                                }

                        )
                        val likes = sewMethod.like.toString()
                        val(likesCount)=createRefs()

                        Text(
                            text = likes,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.End)
                                .constrainAs(likesCount){
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