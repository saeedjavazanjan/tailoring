package com.saeeed.devejump.project.tailoring

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saeeed.devejump.project.tailoring.datastore.AppDataStore
import com.saeeed.devejump.project.tailoring.presentation.components.BottomNavigationBar
import com.saeeed.devejump.project.tailoring.presentation.components.ExitAlertDialog
import com.saeeed.devejump.project.tailoring.presentation.components.Navigation
import com.saeeed.devejump.project.tailoring.presentation.components.currentRoute
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.components.BottomNavItem
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
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

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
            val topBarState = rememberSaveable { (mutableStateOf(true)) }
            val scaffoldState= rememberScaffoldState()

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()


            when (navBackStackEntry?.destination?.route) {
                Screen.SewDescription.route + "/{sewId}"-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = false
                    // topBarState.value = true

                }
                Screen.MoreOfBests.route + "/{type}"->{
                    bottomBarState.value = false

                }
                Screen.Home.route-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = true
                    // topBarState.value = true

                }
                Screen.Profile.route-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = true
                    // topBarState.value = true

                }
                Screen.Profile.route+ "/{authorID}"-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = false
                    // topBarState.value = true

                }
                Screen.Courses.route-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = true
                    // topBarState.value = true

                }
                Screen.Search.route-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = true
                    // topBarState.value = true

                }
                Screen.Splash.route-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = false
                    // topBarState.value = true

                }
                Screen.TimeLine.route-> {
                    // Show BottomBar and TopBar
                    bottomBarState.value = true
                    // topBarState.value = true

                }
            }


            val openDialog = remember { mutableStateOf(false) }
            BackHandler(enabled = (currentRoute(navController) == Screen.Home.route)) {

                openDialog.value = true
            }
            Scaffold(
                bottomBar = {

                    BottomNavigationBar(
                        items = listOf(
                            BottomNavItem(
                                name = "کاربران",
                                route = Screen.TimeLine.route,
                                icon = Icons.Default.Face
                            ),
                            BottomNavItem(
                                name = "فروشگاه",
                                route = Screen.Courses.route,
                                icon = Icons.Default.ShoppingCart
                            ),
                            BottomNavItem(
                                name = "خانه",
                                route = Screen.Home.route,
                                icon = Icons.Default.Home
                            ),
                            BottomNavItem(
                                name = "جست و جو",
                                route = Screen.Search.route,
                                icon = Icons.Default.Search
                            ),
                            BottomNavItem(
                                name = "آموزشگاه",
                                route = Screen.School.route,
                                icon = Icons.Default.DateRange
                            )

                        ),
                        navController = navController,
                        onItemClick = {
                            navController.navigate(it.route) {
                                /*  launchSingleTop=true
                                popUpTo(Screen.HomeSubNavigation.route){
                                 //   inclusive=true
                                    saveState = true
                                }
                                restoreState=true*/
                                launchSingleTop = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                            }

                        },
                        bottomBarState = bottomBarState,



                    )
                },
                snackbarHost = {
                    scaffoldState.snackbarHostState
                }
            ) {
                Navigation(
                    appDataStore = appDataStore,
                    connectivityManager = connectivityManager,
                    navController = navController,
                    scaffoldState=scaffoldState

                )
                if (openDialog.value) {
                    ExitAlertDialog(navController, {
                        openDialog.value = it
                    }, {
                        finish()
                    })

                }


                /*   val listViewModel: ListViewModel = viewModel()
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
                    DescriptionScreen(
                        isDarkTheme = appDataStore.isDark.value,
                        isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                        sewId = navBackStackEntry.arguments?.getInt("sewId"),
                        viewModel = descriptionViewModel,
                    )
                }
            }*/

            }
        }
    }

}

