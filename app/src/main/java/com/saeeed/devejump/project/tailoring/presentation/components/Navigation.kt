package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.saeeed.devejump.project.tailoring.datastore.AppDataStore
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.bests.BestsScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.courses.CoursesScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.posts.PostsScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.profile.ProfileScreen
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(
    appDataStore: AppDataStore,
    connectivityManager: ConnectivityManager,
    navController: NavHostController
){
    val listViewModel: ListViewModel = viewModel()
    val descriptionViewModel: DescriptionViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

            composable(Screen.Home.route){
                HomeScreen(
                    isDarkTheme = appDataStore.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    viewModel = homeViewModel,
                    onNavigateToDescriptionScreen = navController::navigate

                    )

            }
            composable(
                route = Screen.SewDescription.route + "/{sewId}",
                arguments = listOf(navArgument("sewId") {
                    type = NavType.IntType
                })
            ) { navBackStackEntry ->
                DescriptionScreen(
                    isDarkTheme = appDataStore.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    sewId = navBackStackEntry.arguments?.getInt("sewId"),
                    viewModel = descriptionViewModel,
                )
            }

        composable(Screen.Profile.route){
            ProfileScreen()
        }
        composable(Screen.Courses.route){
            CoursesScreen()

        }
        composable(Screen.SewList.route){
            ListScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                onToggleTheme = { /*TODO*/ },
                onNavigateToDescriptionScreen = navController::navigate,
                viewModel =listViewModel
            )

        }

        composable(
            route=Screen.MoreOfBests.route + "/{type}",
            arguments = listOf(navArgument("type"){
                type= NavType.StringType
            })
        ){ navBackStackEntry ->
                BestsScreen(type = navBackStackEntry.arguments?.getString("type") )


            }





      /*  composable(route = Screen.SewList.route) { navBackStackEntry ->

            ListScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                onToggleTheme = appDataStore::toggleTheme,
                onNavigateToDescriptionScreen = navController::navigate,
                viewModel = listViewModel,
            )
        }
        composable(
            route = Screen.SewDescription.route + "/{sewId}",
            arguments = listOf(navArgument("sewId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            DescriptionScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                sewId = navBackStackEntry.arguments?.getInt("sewId"),
                viewModel = descriptionViewModel,
            )
        }*/

}


}

