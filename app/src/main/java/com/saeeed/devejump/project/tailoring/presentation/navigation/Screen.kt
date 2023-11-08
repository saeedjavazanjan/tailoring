package com.saeeed.devejump.project.tailoring.presentation.navigation

sealed class Screen(
    val route: String,
) {
    object Posts : Screen("posts")
    object Splash : Screen("splash")

    object SewDescription : Screen("sewDescription")

    object Courses : Screen("courses")
    object MoreOfBests : Screen("moreOfBests")
    object Home : Screen("home")
    object Profile : Screen("profile")
}