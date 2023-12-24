package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.search.PAGE_SIZE
@Composable
fun SewMethodList(
    loading: Boolean,
    scrollState: LazyGridState,
    posts: List<Post>,
    onChangeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerNextPage: () -> Unit,
    onNavigateToDescriptionScreen: (String) -> Unit,
) { Box(modifier = Modifier
    .background(color = MaterialTheme.colorScheme.surface)
) {
    if (loading && posts.isEmpty()) {
       // LoadingRecipeListShimmer(imageHeight = 250.dp,)
    } else if (posts.isEmpty()) {
     //   NothingHere()
    } else {
        LazyVerticalGrid(
            state=scrollState,
            columns = GridCells.Fixed(2) ) {

            itemsIndexed(
                items = posts
            ) { index, sewMethod ->
                onChangeScrollPosition(index)
                if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                    onTriggerNextPage()
                }
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