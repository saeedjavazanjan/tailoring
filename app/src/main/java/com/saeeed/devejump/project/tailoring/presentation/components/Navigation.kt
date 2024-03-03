package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.saeeed.devejump.project.tailoring.datastore.AppDataStore
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.article.ArticleScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.bests.BestsScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.courses.CoursesScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.description.DescriptionViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.home.HomeViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.peoples.followers.FollowersScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.peoples.followers.FollowersViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.peoples.following.FollowingsScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.peoples.following.FollowingsViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.search.ListScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.search.ListViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.profile.ProfileScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.school.SchoolScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.splash.SplashScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.splash.SplashViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.author_profile.UserProfileScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.author_profile.AuthorProfileViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.notifications.NotificationsScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.notifications.NotificationsViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.product_detail.ProductDetailScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.profile.ProfileViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.school.SchoolViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.UploadPostScreen
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.UploadPostViewModel
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun Navigation(
    appDataStore: AppDataStore,
    connectivityManager: ConnectivityManager,
    navController: NavHostController,
    scaffoldState:ScaffoldState,

) {
    val listViewModel: ListViewModel = viewModel()
    val descriptionViewModel: DescriptionViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val splashViewModel: SplashViewModel = viewModel()
    val authorProfileViewModel: AuthorProfileViewModel = viewModel()
    val followersViewModel: FollowersViewModel = viewModel()
    val followingsViewModel: FollowingsViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val uploadPostViewModel: UploadPostViewModel = viewModel()
    val schoolViewModel:SchoolViewModel= viewModel()
    val notificationViewModel:NotificationsViewModel= viewModel()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = splashViewModel,
                onNavigateToHomeScreen = navController::navigate

            )
        }


        composable(Screen.Home.route) {

            HomeScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = homeViewModel,
                onNavigateToDescriptionScreen = navController::navigate,
                onNavigateToBannerDestination = navController::navigate

            )

        }
        composable(Screen.Courses.route) {
            CoursesScreen()

        }
        composable(Screen.School.route) {
            SchoolScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = schoolViewModel,
                onNavigateToBannerDestination = navController::navigate,
                onNavigateToArticleScreen ={
                    navController.navigate(it)
                }

            )

        }
        composable(Screen.AuthorProfile.route) {
            UserProfileScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                onNavigateToDescriptionScreen = navController::navigate,
                onNavigateToFollowersScreen = {
                    navController.navigate(Screen.Followers.route)
                },
                onNavigateToFollowingsScreen = {
                    navController.navigate(Screen.Followings.route)

                },
                viewModel = authorProfileViewModel,
                onNavigateToUploadPostScreen = {
                    navController.navigate(Screen.UploadPost.route+ "/0/upload")
                }
            )

        }
        composable(Screen.Notifications.route){
            NotificationsScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = notificationViewModel,
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = Screen.SewDescription.route + "/{sewId}",
            arguments = listOf(navArgument("sewId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            /*  val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                val viewModel: DescriptionViewModel = viewModel("RecipeListViewModel", factory)*/
            DescriptionScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                sewId = navBackStackEntry.arguments?.getInt("sewId"),
                viewModel = descriptionViewModel,
                scaffoldState = scaffoldState,
                navController=navController,
                onNavigateToProfile = navController::navigate,
                onNavigateToProductDetailScreen= {
                    navController.navigate(Screen.ProductDetail.route)
                },

                onNavigateToUploadPost = {
                    navController.navigate(it)
                }
            )


        }

        composable(
            route = Screen.Profile.route + "/{authorID}",
            arguments = listOf(navArgument("authorID") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getInt("authorID")?.let {
                ProfileScreen(
                    isDarkTheme = appDataStore.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    onNavigateToDescriptionScreen = navController::navigate,
                    onNavigateToFollowersScreen = {
                        navController.navigate(Screen.Followers.route)
                    },
                    onNavigateToFollowingsScreen = {
                        navController.navigate(Screen.Followings.route)

                    },
                    viewModel = profileViewModel,
                    userId =it
                )
            }
        }
        composable(Screen.Followers.route) {
            FollowersScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = followersViewModel,
                onNavigateToProfile = navController::navigate
            )
        }
        composable(Screen.Followings.route) {
            FollowingsScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                viewModel = followingsViewModel,
                onNavigateToProfile = navController::navigate
            )
        }


        composable(
            route = Screen.Article.route + "/{articleID}",
            arguments = listOf(navArgument("articleID") {
                type = NavType.IntType
            })
        ){navBackStackEntry->

            navBackStackEntry.arguments?.getInt("articleID")?.let {
                ArticleScreen(
                    viewModel =schoolViewModel ,
                    id = it,
                    isDarkTheme = appDataStore.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                )
            }
        }






        composable(
            route = Screen.UploadPost.route + "/{postID}/{navigateType}",
            arguments = listOf(
                navArgument("postID") { type = NavType.IntType },
                navArgument("navigateType") { type = NavType.StringType },

                )) {navBackStackEntry->

            UploadPostScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                navigateType=navBackStackEntry.arguments?.getString("navigateType"),
                postId=navBackStackEntry.arguments?.getInt("postID"),
                viewModel = uploadPostViewModel,
                navController = navController,
                onNavigateTpProductDetailScreen = navController::navigate,
                        onNavigateToAuthorProfile={
                            navController.navigate(Screen.AuthorProfile.route)

                        }
            )

        }





        composable(Screen.Search.route) {
            ListScreen(
                isDarkTheme = appDataStore.isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                onToggleTheme = { /*TODO*/ },
                onNavigateToDescriptionScreen = navController::navigate,
                viewModel = listViewModel
            )

        }

        composable(
            route = Screen.MoreOfBests.route + "/{type}",
            arguments = listOf(navArgument("type") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            BestsScreen(type = navBackStackEntry.arguments?.getString("type"))


        }

        composable(route = Screen.ProductDetail.route) { navBackStackEntry ->
                ProductDetailScreen(
                    viewModel = descriptionViewModel,
                    isDarkTheme = appDataStore.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    scaffoldState = scaffoldState
                )

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

