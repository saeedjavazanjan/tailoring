package com.saeeed.devejump.project.tailoring.presentation.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Post

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostCart(
    post:Post,
    onClick:()-> Unit
) {
    if (post.postType.equals("video")){
        Box( modifier = Modifier
            .aspectRatio(1f)
            .padding(3.dp).clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            val requestbuilder= Glide.with(LocalView.current)
            GlideImage(
                model = post.videoUrl,
                loading =  placeholder(R.drawable.empty_plate),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            ){
                it.thumbnail(requestbuilder.asDrawable().load(Uri.parse(post.videoUrl)))
            }
            Icon(
                painter = painterResource(
                    R.drawable.baseline_play_circle_outline_24),
                contentDescription = "Radio button icon",
                tint = Color.White
            )
        }

    }else{
        GlideImage(
            model = post.featuredImage[0],
            loading = placeholder(R.drawable.empty_plate),
            contentDescription = "",
            modifier = Modifier
                .aspectRatio(1f)
                .padding(3.dp).clickable(onClick=onClick),
            contentScale = ContentScale.Crop,
        )

    }






}