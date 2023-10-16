package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.list.PAGE_SIZE
@Composable
fun SewMethodList(
    loading: Boolean,
    sewMethods: List<SewMethod>,
    onChangeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerNextPage: () -> Unit,
    onNavigateToDescriptionScreen: (String) -> Unit,
) { Box(modifier = Modifier
    .background(color = MaterialTheme.colorScheme.surface)
) {
    if (loading && sewMethods.isEmpty()) {
       // LoadingRecipeListShimmer(imageHeight = 250.dp,)
    } else if (sewMethods.isEmpty()) {
     //   NothingHere()
    } else {
        LazyColumn {
            itemsIndexed(
                items = sewMethods
            ) { index, sewMethod ->
                onChangeScrollPosition(index)
                if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                    onTriggerNextPage()
                }
                SewMethodCard(
                    sewMethod = sewMethod,
                    onClick = {
                        val route = Screen.SewList.route + "/${sewMethod.id}"
                        onNavigateToDescriptionScreen(route)
                    }
                )
            }
        }
    }
}
}