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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import org.w3c.dom.Comment

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommentCard(
   // comment: CommentOnPost,
    text:String,
    edit:()->Unit,
    report:()-> Unit
    ){

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
        ConstraintLayout {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()
                    .padding(top = 12.dp, start = 8.dp, end = 8.dp),
            ) {
                val (avatarHolder,authorName,date,commentText) = createRefs()

                GlideImage(
                    model = "",
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
                val author=""
                //  val(authorText)=createRefs()
                Text(
                    text = author,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .padding(10.dp)
                        .constrainAs(authorName) {
                            start.linkTo(avatarHolder.end)
                            bottom.linkTo(avatarHolder.bottom)
                            top.linkTo(avatarHolder.top)
                        },
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            val updated = ""
            Text(
                text = updated,
                color= Color.Gray,
                modifier = Modifier
                    .padding(start = 10.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                ,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }


}