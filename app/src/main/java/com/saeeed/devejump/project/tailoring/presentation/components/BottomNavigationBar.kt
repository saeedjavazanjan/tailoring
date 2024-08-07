package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    items:List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier=Modifier,
    onItemClick:(BottomNavItem)->Unit,
    bottomBarState :MutableState<Boolean>
){
  //  val BackStackEntry=navController.currentBackStackEntryAsState()

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            BottomNavigation(
                modifier = modifier,
                backgroundColor = Color.DarkGray,
                elevation = 5.dp
            ) {
                items.forEach { item ->
                    val selected = item.route == currentRoute

                    BottomNavigationItem(
                        selected = selected,
                        onClick = { onItemClick(item) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Gray,
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (item.badgeCount > 0) {
                                    /*   BadgedBox(
                            content ={
                                Text(text = item.badgeCount.toString())
                            }

                        ) {
                            Icon(imageVector = item.icon, contentDescription = null)
                        }*/
                                } else {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null
                                    )
                                }
                                if (selected) {
                                    Text(
                                        text = item.name,
                                        textAlign = TextAlign.Center,
                                        fontSize = 10.sp
                                    )
                                }

                            }

                        }
                    )

                }

            }
        })
}