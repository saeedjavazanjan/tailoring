package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.saeeed.devejump.project.tailoring.MainActivity
import com.saeeed.devejump.project.tailoring.datastore.AppDataStore
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.bests.BestsScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.courses.CoursesScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.post.ListScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.post.ListViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.profile.ProfileScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.school.SchoolScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.splash.SplashScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.splash.SplashViewModel
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun Navigation(
    appDataStore: AppDataStore,
    connectivityManager: ConnectivityManager,
    navController: NavHostController,
    scaffoldState:ScaffoldState,

){
    val listViewModel: ListViewModel =viewModel()
    val descriptionViewModel: DescriptionViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val splashViewModel:SplashViewModel= viewModel()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route){
            SplashScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = splashViewModel,
                onNavigateToHomeScreen = navController::navigate

            )
        }
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
            ) {navBackStackEntry->
              /*  val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                val viewModel: DescriptionViewModel = viewModel("RecipeListViewModel", factory)*/
                    DescriptionScreen(
                        isDarkTheme = appDataStore.isDark.value,
                        isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                        sewId = navBackStackEntry.arguments?.getInt("sewId"),
                        viewModel = descriptionViewModel,
                        scaffoldState = scaffoldState,
                        onNavigateToProfile = navController::navigate

                        )


            }

        composable(
            route = Screen.Profile.route + "/{authorID}",
            arguments = listOf(navArgument("authorID") {
                type = NavType.StringType
            })
        ){navBackStackEntry ->
            navBackStackEntry.arguments?.getString("authorID")?.let { ProfileScreen(author = it) }
        }
        composable(Screen.Courses.route){
            CoursesScreen()

        }
        composable(Screen.School.route){
            SchoolScreen()

        }
        composable(Screen.Posts.route){
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

