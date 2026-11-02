package com.gio.sabor_android.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gio.sabor_android.data.repository.AuthRepository
import com.gio.sabor_android.ui.auth.LoginScreen
import com.gio.sabor_android.ui.auth.RegisterScreen
import com.gio.sabor_android.ui.explore.ExploreScreen
import com.gio.sabor_android.ui.favorites.FavoritesScreen
import com.gio.sabor_android.ui.home.HomeScreen
import com.gio.sabor_android.ui.profile.ProfileScreen
import com.gio.sabor_android.ui.profile.SettingsScreen
import com.gio.sabor_android.ui.theme.AvocadoGreen
import com.gio.sabor_android.ui.theme.NarangaOrange

// Sealed class containing all app routes and bottom navigation metadata
sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Login : Screen("login")
    object Register : Screen("register")

    // Bottom Nav Tabs
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", Icons.Default.Explore)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Settings : Screen("settings") // Added settings route
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    val authRepository = AuthRepository()
    // Check if user is already logged in to set the starting destination
    val startDestination = if (authRepository.getCurrentUser() != null) {
        "main_dashboard"
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen Route
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main_dashboard") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    // TODO: Implement Google Sign-In flow if needed
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Register Screen Route
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("main_dashboard") {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onGoogleSignUpClick = {
                    // TODO: Implement Google Sign-Up flow if needed

                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Main Authenticated Dashboard with Bottom Navigation
        composable("main_dashboard") {
            MainDashboardScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo("main_dashboard") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainDashboardScreen(
    onLogout: () -> Unit = {}
) {
    val bottomNavController = rememberNavController()
    val authRepository = AuthRepository()

    val items = listOf(
        Screen.Home,
        Screen.Explore,
        Screen.Favorites,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = AvocadoGreen
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                        label = { screen.title?.let { Text(it) } },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NarangaOrange,       // Active icon color
                            selectedTextColor = NarangaOrange,      // Active label color
                            unselectedIconColor = AvocadoGreen.copy(alpha = 0.6f), // Inactive icon color
                            unselectedTextColor = AvocadoGreen.copy(alpha = 0.6f), // Inactive label color
                            indicatorColor = NarangaOrange.copy(alpha = 0.15f)     // Pill background behind selected icon
                        ),
                        onClick = {
                            if (currentRoute != screen.route) {
                                bottomNavController.navigate(screen.route) {
                                    popUpTo(bottomNavController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home Tab
            composable(Screen.Home.route) {
                HomeScreen(
                    userName = authRepository.getCurrentUser()?.email?.substringBefore("@")?.uppercase() ?: "GIO",
                    onRecipeClick = {
                        // TODO: Navigate to Recipe Detail Screen
                    }
                )
            }
            // Explore Tab
            composable(Screen.Explore.route) {
                ExploreScreen()
            }
            // Favorites Tab
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
            // Profile Tab
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = {
                        bottomNavController.navigate(Screen.Settings.route)
                    }
                )
            }
            // Settings Screen (pushed from Profile gear icon)
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = { bottomNavController.popBackStack() },
                    onLogoutSuccess = onLogout
                )
            }
        }
    }
}