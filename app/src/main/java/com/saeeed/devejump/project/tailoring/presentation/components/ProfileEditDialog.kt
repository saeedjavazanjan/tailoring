package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import androidx.compose.material3.Card as Card1

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileEditDialog(
    showDialog: (Boolean) -> Unit,
    userData: UserData

) {

    val userName= remember {
        mutableStateOf(userData.userName)
    }
    var expanded = remember {
        listOf(
            mutableStateOf(false),
            mutableStateOf(false),
            ) }

    Dialog(onDismissRequest = { showDialog(false) }) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {

                Column {

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(id = R.string.profile_edit_title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(onClick = {
                                expanded[0].value = !expanded[0].value
                            })
                    ) {
                        Column(

                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = stringResource(id = R.string.profile_edit_user_name),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (expanded[0].value) {
                                TextField(
                                    value =userName.value
                                    ,
                                    onValueChange ={
                                        userName.value=it
                                    }
                                )
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(onClick = {
                                expanded[1].value = !expanded[1].value
                            })
                    ) {
                        Column(

                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = stringResource(id = R.string.profile_edit_avatar),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (expanded[1].value) {

                                ConstraintLayout(
                                modifier = Modifier.padding(8.dp)
                                ) {
                                    val (avatarHolder,selectImageButton) = createRefs()

                                    GlideImage(
                                        model = userData.avatar,
                                        loading = placeholder(R.drawable.empty_plate),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(50.dp)
                                            .fillMaxWidth(0.4f)
                                            .clip(CircleShape)
                                            .constrainAs(avatarHolder) {
                                                start.linkTo(parent.start)
                                                top.linkTo(parent.top)
                                            },
                                        contentScale = ContentScale.Crop,
                                    )
                                    Button(
                                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                                        shape= RoundedCornerShape(5.dp) ,
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .fillMaxWidth(0.8f)
                                            .constrainAs(selectImageButton) {
                                                start.linkTo(avatarHolder.end)
                                                top.linkTo(avatarHolder.top)
                                                bottom.linkTo(avatarHolder.bottom)
                                                end.linkTo(parent.end)

                                            },
                                        onClick = {
                                          //  showDialog.value=true

                                        }
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(5.dp),
                                            text = stringResource(id = R.string.profile_edit_choose_photo),
                                            color = Color.DarkGray
                                        )
                                        Icon(Icons.Default.AccountCircle, contentDescription = null)

                                    }
                                }
                            }
                        }
                    }

                }

            }

        }
    }
}