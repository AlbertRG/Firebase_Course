package com.albertrg.firebasecourse.ui.screens.signIn

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val user: String = "",
    val password: String = "",
    var isPasswordVisible: Boolean = false,
    val passSuppText: String = "",
    val isPasswordError: Boolean = false,
    val isBiometricEnabled: Boolean = false
)