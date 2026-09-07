package com.whoanimal.app.ui.navigation

/**
 * Destinos y rutas centralizadas de la navegación de WHO Animal.
 *
 * Evita el uso de cadenas mágicas dispersas y estructura el flujo del producto:
 * Splash -> Home -> [Capture -> IdentificationResult, Collection]
 */
sealed class NavDestination(val route: String) {
    object Splash : NavDestination("splash")
    object Welcome : NavDestination("welcome")
    object CreateProfile : NavDestination("create_profile")
    object Home : NavDestination("home")
    object Capture : NavDestination("capture")
    object IdentificationResult : NavDestination("identification_result")
    object CardReview : NavDestination("card_review")
    object Collection : NavDestination("collection")
}

