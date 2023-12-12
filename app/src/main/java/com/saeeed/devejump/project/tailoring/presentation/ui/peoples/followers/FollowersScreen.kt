package com.saeeed.devejump.project.tailoring.presentation.ui.peoples.followers

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.components.FollowerCard
import com.saeeed.devejump.project.tailoring.presentation.ui.search.PAGE_SIZE
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FollowersScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: FollowersViewModel,
    onNavigateToProfile: (String) -> Unit,
    ) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()
    val followers=viewModel.followers
    val page = viewModel.page.value
    val query = viewModel.query.value

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            topBar = {
                SearchBar(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(FollowersEvent.NewEvent)
                    }
                )
            }

        ) {
            LazyColumn{
                itemsIndexed(
                    items = followers.value
                ) { index, follower ->
                    viewModel.onChangeScrollPosition(index)
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                       viewModel.onTriggerEvent(FollowersEvent.NextPageEvent)
                    }
                    FollowerCard(
                      follower=  follower,

                    )
                }
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
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                placeholderColor = MaterialTheme.colorScheme.surface,
                disabledPlaceholderColor = MaterialTheme.colorScheme.surface
            )

        )
    }

}