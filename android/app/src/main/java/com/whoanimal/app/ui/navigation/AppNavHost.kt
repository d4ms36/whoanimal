package com.whoanimal.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomProfileRepository
import com.whoanimal.app.domain.model.IdentificationResultContract
import com.whoanimal.app.domain.repository.ProfileRepository
import com.whoanimal.app.ui.screens.capture.CameraCaptureScreen
import com.whoanimal.app.ui.screens.capture.IdentificationResultScreen
import com.whoanimal.app.ui.screens.collection.CollectionPlaceholderScreen
import com.whoanimal.app.ui.screens.home.HomeScreen
import com.whoanimal.app.ui.screens.profile.CreateProfileScreen
import com.whoanimal.app.ui.screens.splash.SplashScreen
import com.whoanimal.app.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestination.Splash.route,
    profileRepository: ProfileRepository? = null
) {
    val context = LocalContext.current
    val effectiveProfileRepository = profileRepository ?: remember(context) {
        RoomProfileRepository(WhoAnimalDatabase.getInstance(context).profileDao())
    }

    var currentIdentificationResult by remember { mutableStateOf<IdentificationResultContract?>(null) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestination.Splash.route) {
            SplashScreen(
                profileRepository = effectiveProfileRepository,
                onNavigateToHome = {
                    navController.navigate(NavDestination.Home.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToWelcome = {
                    navController.navigate(NavDestination.Welcome.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestination.Welcome.route) {
            WelcomeScreen(
                onStartExpedition = {
                    navController.navigate(NavDestination.CreateProfile.route)
                }
            )
        }

        composable(NavDestination.CreateProfile.route) {
            CreateProfileScreen(
                profileRepository = effectiveProfileRepository,
                onProfileCreated = {
                    navController.navigate(NavDestination.Home.route) {
                        popUpTo(NavDestination.Welcome.route) { inclusive = true }
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
                },
                profileRepository = effectiveProfileRepository
            )
        }

        composable(NavDestination.Capture.route) {
            CameraCaptureScreen(
                onNavigateToResult = { result ->
                    currentIdentificationResult = result
                    navController.navigate(NavDestination.IdentificationResult.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestination.IdentificationResult.route) {
            IdentificationResultScreen(
                result = currentIdentificationResult,
                onAcceptDecision = {
                    // En Alpha 0.1, registrar la decisión aceptada sin generar Capture persistente
                },
                onDiscardDecision = {
                    currentIdentificationResult = null
                    navController.popBackStack(NavDestination.Home.route, inclusive = false)
                },
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
