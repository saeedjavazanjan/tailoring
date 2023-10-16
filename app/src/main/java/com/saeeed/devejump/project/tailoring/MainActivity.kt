package com.saeeed.devejump.project.tailoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saeeed.devejump.project.tailoring.datastore.AppDataStore
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.list.ListViewModel
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var appDataStore: AppDataStore

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listViewModel: ListViewModel = viewModel()
            val descriptionViewModel: DescriptionViewModel = viewModel()

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.SewList.route) {
                composable(route = Screen.SewList.route) { navBackStackEntry ->

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
                    RecipeDetailScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                        recipeId = navBackStackEntry.arguments?.getInt("recipeId"),
                        viewModel = viewModel,
                    )
                }
            }

        }
    }


}

