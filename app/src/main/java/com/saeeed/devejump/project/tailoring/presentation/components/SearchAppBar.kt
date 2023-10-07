package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.presentation.ui.list.Category

@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    categories: List<Category>,
    selectedCategory: Category?,
    onSelectedCategoryChanged: (String) -> Unit,
    scrollPosition: Float,
    onChangeScrollPosition: (Float) -> Unit,
) = Surface(
    modifier = Modifier
        .fillMaxWidth()
    ,
    color = Color.White,
    elevation = 8.dp,
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
                leadingIcon = {
                    Icon(Icons.Filled.Search)
                },
                onImeActionPerformed = { action, softKeyboardController ->
                    if (action == ImeAction.Done) {
                        onExecuteSearch()
                        softKeyboardController?.hideSoftwareKeyboard()
                    }
                },
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                backgroundColor = MaterialTheme.colors.surface
            )
        }
        val scrollState = rememberScrollState()
        ScrollableRow(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp)
            ,
            scrollState = scrollState,
        ) {

            // restore scroll position after rotation
            scrollState.scrollTo(scrollPosition)

            for(category in categories){
                FoodCategoryChip(
                    category = category.value,
                    isSelected = selectedCategory == category,
                    onSelectedCategoryChanged = {
                        onChangeScrollPosition(scrollState.value)
                        onSelectedCategoryChanged(it)
                    },
                    onExecuteSearch = {
                        onExecuteSearch()
                    },
                )
            }
        }
    }
}

fun Icon(search: Any) {

}
