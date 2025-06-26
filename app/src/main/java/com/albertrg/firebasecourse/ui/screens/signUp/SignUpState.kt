package com.albertrg.firebasecourse.ui.screens.signUp

import androidx.compose.runtime.Stable

@Stable
data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isPasswordError: Boolean = false,
    val passSuppText: String = "",
    var isPasswordVisible: Boolean = false
)