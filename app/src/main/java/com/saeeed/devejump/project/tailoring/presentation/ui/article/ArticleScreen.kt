package com.saeeed.devejump.project.tailoring.presentation.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.ui.school.SchoolViewModel
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArticleScreen(
    viewModel: SchoolViewModel,
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    id:Int
){
    val articles=viewModel.articles.value
    val loading=viewModel.loading.value
    val dialogQueue=viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()

    val article=articles.filter {
        it.id==id
    }
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
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        text = HtmlCompat.fromHtml(article[0].html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            .toString()
                    )
                }

            }
        }


}