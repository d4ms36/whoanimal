package com.whoanimal.app.ui.navigation

/**
 * Destinos y rutas centralizadas de la navegación de WHO Animal.
 *
 * Evita el uso de cadenas mágicas dispersas y estructura el flujo del producto:
 * Splash -> Home -> [Capture -> IdentificationResult, Collection]
 */
sealed class NavDestination(val route: String) {
    object Splash : NavDestination("splash")
    object Home : NavDestination("home")
    object Capture : NavDestination("capture")
    object IdentificationResult : NavDestination("identification_result")
    object Collection : NavDestination("collection")
}
