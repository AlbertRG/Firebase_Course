package com.albertrg.firebasecourse.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.albertrg.firebasecourse.ui.composables.SignBackground

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    navigateToSignIn: () -> Unit,
    navigateToHome: () -> Unit

) {
    val splashState by splashViewModel.splashState.collectAsStateWithLifecycle()

    splashState.isSignedIn?.let { isSignedIn ->
        if (isSignedIn) {
            navigateToHome()
        } else {
            navigateToSignIn()
        }
    }

    SignBackground()

}