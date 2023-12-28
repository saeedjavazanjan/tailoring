package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.saeeed.devejump.project.tailoring.R

@Composable
fun TopBar(
    topBarState: MutableState<Boolean>,
    onNotifClicked:()->Unit
){

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {


            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.DarkGray,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val (menu) = createRefs()
                        IconButton(
                            modifier = Modifier
                                .constrainAs(menu) {
                                    start.linkTo(parent.start)
                                    linkTo(top = parent.top, bottom = parent.bottom)
                                },
                            onClick = {

                            },
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = null, tint = Color.White)
                        }
                        val (title) = createRefs()
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier.constrainAs(title) {
                                start.linkTo(menu.end)
                                linkTo(top = parent.top, bottom = parent.bottom)
                            }
                        )
                        val (notification) = createRefs()
                        IconButton(
                            modifier = Modifier
                                .constrainAs(notification) {
                                    end.linkTo(parent.end)
                                    linkTo(top = parent.top, bottom = parent.bottom)
                                },
                            onClick = {
                                onNotifClicked()
                            },
                        ) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                    }
                }


            }

        })
}