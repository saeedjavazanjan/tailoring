package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostCart(post:SewMethod) {
    GlideImage(
        model = post.featuredImage[0],
        loading = placeholder(R.drawable.empty_plate),
        contentDescription = "",
        modifier = Modifier
            .height(80.dp)
            .width(80.dp)
            .fillMaxWidth(0.4f)
            .padding(start = 5.dp,end=5.dp),
        contentScale = ContentScale.Crop,
    )


}