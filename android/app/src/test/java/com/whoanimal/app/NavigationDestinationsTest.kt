package com.whoanimal.app

import com.whoanimal.app.ui.navigation.NavDestination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationDestinationsTest {

    @Test
    fun testNavigationDestinationsAreUniqueAndNonEmpty() {
        val destinations = listOf(
            NavDestination.Splash.route,
            NavDestination.Home.route,
            NavDestination.Capture.route,
            NavDestination.Collection.route
        )

        // All routes must be non-empty
        destinations.forEach { route ->
            assertTrue("Route must not be empty", route.isNotBlank())
        }

        // All routes must be distinct
        val uniqueRoutes = destinations.toSet()
        assertEquals("All destination routes must be unique", destinations.size, uniqueRoutes.size)
    }

    @Test
    fun testStandardDestinationNames() {
        assertEquals("splash", NavDestination.Splash.route)
        assertEquals("home", NavDestination.Home.route)
        assertEquals("capture", NavDestination.Capture.route)
        assertEquals("collection", NavDestination.Collection.route)
    }
}
