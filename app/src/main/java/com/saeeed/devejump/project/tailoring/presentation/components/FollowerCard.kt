package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Peoples

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun  FollowerCard(
    follower: Peoples,
)  {
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
            .height(100.dp),
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
                    model = follower.avatar,
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
                val author = follower.userName
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


            }

        }





    }

}