package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.cash.model.CommentEntity
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.utils.USERID

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommentCard(
    comment: CommentEntity,
    edit:()->Unit,
    report:()-> Unit,
    removeComment:()->Unit
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
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = 6.dp,
                    top = 6.dp,
                    start = 10.dp,
                    end = 10.dp
                )
        ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight()
                        .padding(top = 12.dp, start = 8.dp, end = 8.dp),
                ) {
                    val (avatarHolder, authorName, date, commentText,reportButton,editButton) = createRefs()

                    GlideImage(
                        model = comment.avatar,
                        loading = placeholder(R.drawable.empty_plate),
                        contentDescription = "",
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clip(CircleShape)
                            .constrainAs(avatarHolder) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(commentText.top)
                                end.linkTo(authorName.start)
                            },
                        contentScale = ContentScale.Crop,
                    )
                    val author = comment.userName
                    Text(
                        text = author,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .padding(10.dp)
                            .constrainAs(authorName) {
                                start.linkTo(avatarHolder.end)
                                bottom.linkTo(avatarHolder.bottom)
                                top.linkTo(avatarHolder.top)
                                end.linkTo(avatarHolder.end)
                            },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = comment.comment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                            .constrainAs(commentText) {
                                top.linkTo(avatarHolder.bottom)
                                start.linkTo(parent.start)

                            },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    val updated = comment.date
                    Text(
                        text = updated,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .constrainAs(date) {

                            },
                        style = MaterialTheme.typography.bodySmall
                    )

                    if (comment.userId== USERID) {
                        TextButton(
                            modifier = Modifier
                                .constrainAs(editButton) {
                                    top.linkTo(commentText.bottom)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(reportButton.start)
                                },


                            onClick = {
                                edit()

                            }) {
                            Text(
                                text = "ویرایش",
                                style = MaterialTheme.typography.bodySmall


                            )

                        }
                        TextButton(
                            modifier = Modifier
                                .constrainAs(reportButton) {
                                    top.linkTo(commentText.bottom)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                },


                            onClick = {
                               removeComment()
                            }){
                            Text(
                                text = "حذف نظر",
                                style = MaterialTheme.typography.bodySmall,

                            )

                        }

                    }else{
                        TextButton(
                            modifier = Modifier
                                .constrainAs(reportButton) {
                                    top.linkTo(commentText.bottom)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                },


                            onClick = {
                                report()
                            }){
                            Text(
                                text = "گزارش این نظر",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray

                            )

                        }
                    }



                    }

                }





            }


    }

