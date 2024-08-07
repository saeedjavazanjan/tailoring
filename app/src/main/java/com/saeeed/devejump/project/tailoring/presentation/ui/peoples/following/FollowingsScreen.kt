package com.saeeed.devejump.project.tailoring.presentation.ui.peoples.following

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.components.PeopleCard
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import com.saeeed.devejump.project.tailoring.utils.POSTS_PAGINATION_PAGE_SIZE

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FollowingsScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: FollowingsViewModel,
    onNavigateToProfile: (String) -> Unit,
    ) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()
    val followers=viewModel.followings
    val page = viewModel.page.value
    val query = viewModel.query.value

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            topBar = {
                SearchBar(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(FollowingsEvent.NewEvent)
                    }
                )
            }

        ) {
            LazyColumn{
                itemsIndexed(
                    items = followers.value
                ) { index, follower ->
                    viewModel.onChangeScrollPosition(index)
                    if ((index + 1) >= (page * POSTS_PAGINATION_PAGE_SIZE) && !loading) {
                       viewModel.onTriggerEvent(FollowingsEvent.NextPageEvent)
                    }
                    PeopleCard(
                      people=  follower,
                        onNavigateToUserProfile = {
                            val route = Screen.Profile.route + "/${follower.userId}"
                            onNavigateToProfile(route)

                        }

                    )
                }
            }
        }
        
    }

        
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query:String,
    onQueryChanged : (String)->Unit,
    onExecuteSearch :() -> Unit,
){
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = query,

            onValueChange = {
                onQueryChanged(it)
            },
            label = {
                Text(text = "جستجو")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onExecuteSearch()
                }
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, "")
            },
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Medium),


            )
    }

}