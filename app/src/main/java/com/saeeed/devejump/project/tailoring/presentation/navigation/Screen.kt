package com.saeeed.devejump.project.tailoring.presentation.navigation

sealed class Screen(
    val route: String,
) {
    object Search : Screen("search")
    object Splash : Screen("splash")

    object SewDescription : Screen("sewDescription")

    object Courses : Screen("courses")
    object MoreOfBests : Screen("moreOfBests")
    object Home : Screen("home")

    object Notifications:Screen("notifications")
    object Profile : Screen("profile")
    object School : Screen("school")
    object Followers:Screen("followers")
    object Followings:Screen("followings")

    object Article:Screen("article")
    object AuthorProfile : Screen("authorProfile")
    object UploadPost:Screen("uploadPost")
    object ProductDetail:Screen("productDetail")

}