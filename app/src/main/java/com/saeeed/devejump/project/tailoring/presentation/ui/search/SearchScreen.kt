package com.saeeed.devejump.project.tailoring.presentation.ui.search

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.saeeed.devejump.project.tailoring.presentation.components.SearchAppBar
import com.saeeed.devejump.project.tailoring.presentation.components.SewMethodList
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import com.saeeed.devejump.project.tailoring.utils.TAG
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun ListScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToDescriptionScreen: (String) -> Unit,
    viewModel: ListViewModel,
) {
    Log.d(TAG, "SewListScreen: ${viewModel}")

    val sewMethods = viewModel.methods.value

    val query = viewModel.query.value

    val selectedCategory = viewModel.selectedCategory.value

    val loading = viewModel.loading.value

    val page = viewModel.page.value

    val dialogQueue = viewModel.dialogQueue

    val scaffoldState = rememberScaffoldState()

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Scaffold(
            topBar = {
                SearchAppBar(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(SearchEvent.NewSearchEvent)
                    },
                    categories = getAllCategories(),
                    selectedCategory = selectedCategory,
                    onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                )
            }

        ){

            Column(
                Modifier.padding(it)
            ) {

                    SewMethodList(
                        loading = loading,
                        sewMethods = sewMethods,
                        onChangeScrollPosition = viewModel::onChangeCategoryScrollPosition,
                        page = page,
                        onTriggerNextPage = { viewModel.onTriggerEvent(SearchEvent.NextPageEvent) },
                        onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,

                        )
                }
            }


        }
    }
}