package com.saeeed.devejump.project.tailoring.presentation.navigation

sealed class Screen(
    val route: String,
){
    object SewList: Screen("sewList")

    object SewDescription: Screen("sewDescription")

    object Courses:Screen("courses")

    object Home:Screen("home")

    object Posts:Screen("posts")

    object Profile:Screen("profile")
}