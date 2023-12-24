package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalGlideComposeApi::class)
@ExperimentalCoroutinesApi
@Composable
fun BannerCard(
    banner:Banner,
    onClick: () -> Unit,
){
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp,
            )
            .fillMaxWidth()
            .height(225.dp)
            .clickable(onClick = {
                onClick()
            }),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomStart,
        ) {
            banner.imageURL?.let { url ->
                GlideImage(
                    model = url,
                    loading =  placeholder(R.drawable.empty_plate),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp),
                    contentScale = ContentScale.Crop,
                )


            }
            banner.title?.let { title ->
                Box(
                    contentAlignment = Alignment.Center,

                    ){
                    Text(
                        text = title,
                        color= Color.White,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start)
                            .padding(10.dp)
                            .drawBehind {
                                        drawRect(color = Color.DarkGray)
                            }
                        ,
                        fontSize = 15.sp,
                        style =MaterialTheme.typography.bodyMedium

                    )
                }


            }
        }

    }
}