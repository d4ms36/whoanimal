package com.whoanimal.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whoanimal.app.ui.screens.capture.CapturePlaceholderScreen
import com.whoanimal.app.ui.screens.collection.CollectionPlaceholderScreen
import com.whoanimal.app.ui.screens.home.HomeScreen
import com.whoanimal.app.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestination.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestination.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(NavDestination.Home.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestination.Home.route) {
            HomeScreen(
                onNavigateToCapture = {
                    navController.navigate(NavDestination.Capture.route)
                },
                onNavigateToCollection = {
                    navController.navigate(NavDestination.Collection.route)
                }
            )
        }

        composable(NavDestination.Capture.route) {
            CapturePlaceholderScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestination.Collection.route) {
            CollectionPlaceholderScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
