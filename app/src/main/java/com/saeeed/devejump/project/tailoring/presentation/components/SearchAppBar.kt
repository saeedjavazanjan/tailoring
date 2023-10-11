package com.saeeed.devejump.project.tailoring.presentation.components

import CategoryChip
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.ui.list.Category
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    categories: List<Category>,
    selectedCategory: Category?,
    onSelectedCategoryChanged: (String) -> Unit,
    scrollPosition: Int,
    onChangeScrollPosition: (Int) -> Unit,
) = Surface(
    modifier = Modifier
        .fillMaxWidth()
    ,
    color = Color.White,
    shadowElevation = 8.dp,
){
    Column{
        Row(modifier = Modifier.fillMaxWidth()){
            TextField(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .padding(8.dp)
                ,
                value = query,

                onValueChange = {
                    onQueryChanged(it)
                },
                label = {
                    Text(text = "Search")
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
                    Icon(Icons.Filled.Search)
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor=MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.surface,
                    disabledPlaceholderColor= MaterialTheme.colorScheme.surface)

            )

        }
        val scrollState = rememberLazyListState()
        val coroutineScope= rememberCoroutineScope()
        LazyRow(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp),
            state = scrollState
        ) {
            coroutineScope.launch {
                scrollState.scrollToItem(scrollPosition,0)

            }

        }
        for(category in categories){
            CategoryChip(
                category = category.value,
                isSelected = selectedCategory == category,
                onSelectedCategoryChanged = {
                    onChangeScrollPosition(scrollState.hashCode())
                    onSelectedCategoryChanged(it)
                },
                onExecuteSearch = {
                    onExecuteSearch()
                },
            )
        }

        }

}

fun Icon(search: Any) {

}
