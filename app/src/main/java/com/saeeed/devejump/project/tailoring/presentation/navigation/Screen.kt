package com.saeeed.devejump.project.tailoring.presentation.navigation

sealed class Screen(
    val route: String,
){
    object SewList: Screen("sewList")

    object SewDescription: Screen("sewDescription")
}