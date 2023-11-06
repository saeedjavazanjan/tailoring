package com.saeeed.devejump.project.tailoring.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconToggleButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.interactor.description.CheckBookMarkState
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@Composable
fun SewMethodView(
    sewMethod: SewMethod,
    bookMarkState: Boolean,
    save:() -> Unit,
    remove:()-> Unit
) {
    val composableScope = rememberCoroutineScope()
    val snackbarController=SnackbarController(composableScope)
    val bookState= mutableStateOf(bookMarkState)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    AsyncImage(
                        model = sewMethod.featuredImage,
                        contentDescription = sewMethod.title,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    //  if (bookMarkState){
                    IconToggleButton(
                        checked = bookMarkState,
                        onCheckedChange = {
                            if(bookState.value){
                                bookState.value=false
                                remove()
                            }else{
                                bookState.value=true

                                save()
                            }
                        })
                    {
                        Icon(
                            painter = painterResource(if (bookState.value) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                            contentDescription = "Radio button icon",
                            tint = Color(
                                0xFF9B51E0
                            )
                        )
                    }

                     /*   var iconId=R.drawable.baseline_bookmark_24
                        IconButton(onClick = { save()


                        }) {
                            Icon(
                                painter = painterResource(id = iconId ),
                                contentDescription = null
                                , tint = Color.White
                            )

                        }*/
                   /* }else{
                        var iconId=R.drawable.baseline_bookmark_24
                        IconButton(onClick = {
                            if(save() > 0){
                                iconId=R.drawable.baseline_bookmark_border_24

                            }
                        }) {
                            Icon(
                                painter = painterResource(id = iconId ),
                                contentDescription = null
                                , tint = Color.White
                            )

                        }
                    }
*/

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = sewMethod.title,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .wrapContentWidth(Alignment.Start),
                            style = MaterialTheme.typography.bodySmall
                        )
                        val rank = sewMethod.rating.toString()
                        Text(
                            text = rank,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.End)
                                .align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    val updated = sewMethod.dateUpdated
                    Text(
                        text = "Updated ${updated} by ${sewMethod.publisher}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    for (ingredient in sewMethod.ingredients) {
                        Text(
                            text = ingredient,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

}


