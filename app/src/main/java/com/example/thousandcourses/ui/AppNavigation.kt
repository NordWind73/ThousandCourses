package com.example.thousandcourses.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thousandcourses.R
import com.example.thousandcourses.ui.home.details.CourseDetailsViewModel
import com.example.thousandcourses.ui.home.HomeScreen
import com.example.thousandcourses.ui.home.HomeViewModel
import com.example.thousandcourses.ui.home.account.AccountScreen
import com.example.thousandcourses.ui.home.account.AccountViewModel
import com.example.thousandcourses.ui.home.details.DetailsScreen
import com.example.thousandcourses.ui.home.favorite.FavoritesScreen
import com.example.thousandcourses.ui.home.favorite.FavoritesViewModel
import com.example.thousandcourses.ui.login.LoginScreen
import com.example.thousandcourses.ui.login.LoginViewModel
import com.example.thousandcourses.ui.onboarding.OnboardingScreen
import com.example.thousandcourses.ui.onboarding.OnboardingViewModel
import com.example.thousandcourses.ui.register.RegisterScreen
import com.example.thousandcourses.ui.register.RegisterViewModel

enum class MainRoutes {
    ONBORDING, LOGIN, REGISTER, HOME, FAVORITES, ACCOUNT, DETAILS;
}

val bottomNavItems = listOf(
    BottomNavItem(
        route = MainRoutes.HOME.name,
        iconSelected = R.drawable.ic_home_filled,
        iconUnselected = R.drawable.ic_home_outline,
        contentDescription = R.string.nav_home_description,
        label = R.string.nav_home
    ),
    BottomNavItem(
        route = MainRoutes.FAVORITES.name,
        iconSelected = R.drawable.ic_favorite_filled,
        iconUnselected = R.drawable.ic_favorite_border,
        contentDescription = R.string.nav_favorites_description,
        label = R.string.nav_favorites
    ),
    BottomNavItem(
        route = MainRoutes.ACCOUNT.name,
        iconSelected = R.drawable.ic_account_filled,
        iconUnselected = R.drawable.ic_account_outline,
        contentDescription = R.string.nav_account_description,
        label = R.string.nav_account
    )
)

data class BottomNavItem(
    val route: String,
    val iconSelected: Int,
    val iconUnselected: Int,
    val contentDescription: Int,
    val label: Int
)

@Composable
fun AppNavigation(
    onboardingViewModel: OnboardingViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    homeViewModel: HomeViewModel,
    favoritesViewModel: FavoritesViewModel,
    accountViewModel: AccountViewModel,
    detailsViewModel: CourseDetailsViewModel,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = when (currentDestination?.route) {
        MainRoutes.HOME.name, MainRoutes.FAVORITES.name, MainRoutes.ACCOUNT.name -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar(
                    containerColor = colorResource(R.color.black),
                    contentColor = Color.White
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = if (selected) item.iconSelected else item.iconUnselected),
                                    contentDescription = stringResource(item.contentDescription),
                                    tint = if (selected)
                                        colorResource(R.color.accent_green)
                                    else
                                        Color.Gray
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(item.label),
                                    color = if (selected)
                                        colorResource(R.color.accent_green)
                                    else
                                        Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MainRoutes.ONBORDING.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MainRoutes.ONBORDING.name) {
                OnboardingScreen(
                    viewModel = onboardingViewModel,
                    onContinueClick = {
                        navController.navigate(MainRoutes.LOGIN.name) {
                            popUpTo(MainRoutes.ONBORDING.name) { inclusive = true }
                        }
                    }
                )
            }

            composable(MainRoutes.LOGIN.name) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToHome = {
                        navController.navigate(MainRoutes.HOME.name) {
                            popUpTo(MainRoutes.LOGIN.name) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(MainRoutes.REGISTER.name)
                    }
                )
            }

            composable(MainRoutes.REGISTER.name) {
                RegisterScreen(
                    viewModel = registerViewModel,
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(MainRoutes.HOME.name) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onCourseClick = { courseId ->
                        navController.navigate(MainRoutes.DETAILS.name + "/$courseId")
                    }
                )
            }

            composable(MainRoutes.FAVORITES.name) {
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    onCourseClick = { courseId ->
                        navController.navigate(MainRoutes.DETAILS.name + "/$courseId")
                    },
                    onNavigateToHome = {
                        navController.navigate(MainRoutes.HOME.name) {
                            popUpTo(MainRoutes.FAVORITES.name) { inclusive = true }
                        }
                    }
                )
            }

            composable(MainRoutes.ACCOUNT.name) {
                AccountScreen(
                    viewModel = accountViewModel,
                    onLogout = onLogout,
                )
            }

            composable(MainRoutes.DETAILS.name + "/{courseId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""

                DetailsScreen(
                    viewModel = detailsViewModel,
                    courseId = courseId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}