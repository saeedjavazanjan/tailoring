package com.saeeed.devejump.project.tailoring.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListViewModel
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

@Composable
fun Navigation() {

    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SewList.route) {
        composable(route = Screen.SewList.route) { navBackStackEntry ->

            /*RecipeListScreen(
                isDarkTheme = settingsDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                onToggleTheme = settingsDataStore::toggleTheme,
                onNavigateToRecipeDetailScreen = navController::navigate,
                viewModel = viewModel,
            )*/
        }
        composable(
            route = Screen.SewDescription.route + "/{recipeId}",
            arguments = listOf(navArgument("recipeId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
         //   val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
          //  val viewModel: DescriptionViewModel = viewModel("RecipeDetailViewModel", factory)
          /*  RecipeDetailScreen(
                isDarkTheme = settingsDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                recipeId = navBackStackEntry.arguments?.getInt("recipeId"),
                viewModel = viewModel,
            )*/
        }
    }
}