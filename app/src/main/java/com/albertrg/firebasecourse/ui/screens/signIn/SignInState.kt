package com.albertrg.firebasecourse.ui.screens.signIn

import androidx.compose.runtime.Stable

@Stable
data class SignInState(
    val email: String = "curso@gmail.com",
    val password: String = "123Qwerty@",
    val isPasswordError: Boolean = false,
    val passSuppText: String = "",
    var isPasswordVisible: Boolean = false
)