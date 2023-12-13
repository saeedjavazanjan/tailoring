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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun  PeopleCard(
    people: Peoples,
    onNavigateToUserProfile:()->Unit
)  {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                bottom = 10.dp,
                top = 10.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            onNavigateToUserProfile()
        }
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
                    .padding(top = 10.dp, start = 8.dp, end = 8.dp, bottom = 10.dp),
            ) {
                val (avatarHolder, userName , userBio) = createRefs()

                GlideImage(
                    model = people.avatar,
                    loading = placeholder(R.drawable.empty_plate),
                    contentDescription = "",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(CircleShape)
                        .constrainAs(avatarHolder) {
                            start.linkTo(parent.start)

                            top.linkTo(parent.top)
                            bottom.linkTo(userName.top)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = people.userName,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .padding(10.dp)
                        .constrainAs(userName) {
                            start.linkTo(parent.start)
                            bottom.linkTo(userBio.top)
                            top.linkTo(avatarHolder.bottom)
                            end.linkTo(parent.end)
                        },
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = people.bio,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .padding(10.dp)
                        .constrainAs(userBio) {
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            top.linkTo(userName.bottom)
                            end.linkTo(parent.end)
                        },
                    style = MaterialTheme.typography.bodySmall
                )


            }

        }





    }

}