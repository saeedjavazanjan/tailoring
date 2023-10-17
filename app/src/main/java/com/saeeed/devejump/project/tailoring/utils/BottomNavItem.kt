package com.saeeed.devejump.project.tailoring.utils

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name:String,
    val route:String,
    val icon:ImageVector,
    val badgeCount:Int=0
)
