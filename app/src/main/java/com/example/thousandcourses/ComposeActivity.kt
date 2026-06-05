package com.example.thousandcourses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thousandcourses.ui.AppNavigation
import com.example.thousandcourses.ui.home.details.CourseDetailsViewModel
import com.example.thousandcourses.ui.home.HomeViewModel
import com.example.thousandcourses.ui.home.account.AccountViewModel
import com.example.thousandcourses.ui.home.favorite.FavoritesViewModel
import com.example.thousandcourses.ui.login.LoginViewModel
import com.example.thousandcourses.ui.onboarding.OnboardingViewModel
import com.example.thousandcourses.ui.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme (colorScheme = darkColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black)
                {
                    val onboardingViewModel: OnboardingViewModel = viewModel()
                    val loginViewModel: LoginViewModel = viewModel()
                    val registerViewModel: RegisterViewModel = viewModel()
                    val homeViewModel: HomeViewModel = viewModel()
                    val favoritesViewModel: FavoritesViewModel = viewModel()
                    val accountViewModel: AccountViewModel = viewModel()
                    val detailsViewModel: CourseDetailsViewModel = viewModel()


                    AppNavigation(
                        onboardingViewModel = onboardingViewModel,
                        loginViewModel = loginViewModel,
                        registerViewModel = registerViewModel,
                        homeViewModel = homeViewModel,
                        favoritesViewModel = favoritesViewModel,
                        accountViewModel = accountViewModel,
                        detailsViewModel = detailsViewModel,
                        onLogout = {
                            accountViewModel.logout()
                            finishAffinity()
                        }
                    )
                }
            }
        }
    }
}