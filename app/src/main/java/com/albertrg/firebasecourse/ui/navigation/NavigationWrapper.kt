package com.albertrg.firebasecourse.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.albertrg.firebasecourse.ui.screens.signIn.SignInScreen
import com.albertrg.firebasecourse.ui.screens.signIn.SignInViewModel
import com.albertrg.firebasecourse.ui.screens.signUp.SignUpScreen
import com.albertrg.firebasecourse.ui.screens.signUp.SignUpViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SignIn) {

        composable<SignIn> {
            val signInViewModel = hiltViewModel<SignInViewModel>()
            SignInScreen(
                signInViewModel,
                navigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(SignIn) { inclusive = true }
                    }
                },
                navigateToSignUp = {
                    navController.navigate(SignUp)
                }
            )
        }

        composable<SignUp> {
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(
                signUpViewModel,
                navigateToSignIn = {
                    navController.navigate(SignIn) {
                        popUpTo(SignUp) { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {

        }

    }

}