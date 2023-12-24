package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen

@Composable
fun BestsRow(
    loading: Boolean,
    posts: List<Post>,
    onNavigateToDescriptionScreen: (String) -> Unit,
) { Box(modifier = Modifier
    .background(color = MaterialTheme.colorScheme.surface)
) {
    if (loading && posts.isEmpty()) {
        // LoadingRecipeListShimmer(imageHeight = 250.dp,)
    } else if (posts.isEmpty()) {
        //   NothingHere()
    } else {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        LazyRow {
            itemsIndexed(
                items = posts
            ) { index, sewMethod ->
                SewMethodCard(
                    post = sewMethod,
                    onClick = {
                        val route = Screen.SewDescription.route + "/${sewMethod.id}"
                        onNavigateToDescriptionScreen(route)
                    }
                )
            }
        }
    }
    }
}
}