package com.albertrg.firebasecourse.ui.screens.signUp

data class SignUpState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val user: String = "",
    val password: String = "",
    var isPasswordVisible: Boolean = false,
    val passSuppText: String = "",
    val isPasswordError: Boolean = false,
    val isBiometricEnabled: Boolean = false
)