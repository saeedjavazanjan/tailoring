package com.saeeed.devejump.project.tailoring.presentation.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.data.EmptyGroup.data
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.presentation.components.SearchAppBar
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.list.SewListEvent
import com.saeeed.devejump.project.tailoring.presentation.ui.list.getAllCategories
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: HomeViewModel,

    ) {
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
    ) {
        val scrollState = rememberScrollState()

        Scaffold(
            topBar= {
                TopBar()
            }

        ) {
            Box( // or whatever your parent composable is
                modifier = Modifier
                    .scrollable(state = scrollState, orientation = Orientation.Vertical)
            ) {
                LazyRow{
                    itemsIndexed(data) { index, d ->
                        ListItem(d)
                        if (index != data.size - 1) {
                            Divider()
                        }
                    }
                }

            }
        }
    }
}