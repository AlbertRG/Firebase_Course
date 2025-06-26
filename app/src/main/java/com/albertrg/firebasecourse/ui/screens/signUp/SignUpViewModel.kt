package com.albertrg.firebasecourse.ui.screens.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertrg.firebasecourse.R
import com.albertrg.firebasecourse.data.AuthService
import com.albertrg.firebasecourse.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val authService: AuthService
) : ViewModel() {

    private var _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> =
        _signUpState.onStart {
            //init
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SignUpState()
        )

    fun onEmailChanged(uiEmail: String) {
        _signUpState.value = _signUpState.value.copy(email = uiEmail)
    }

    fun onPasswordChanged(uiPassword: String) {
        _signUpState.value = _signUpState.value.copy(password = uiPassword)
        resetPasswordError()
    }


    fun onPasswordVisibilityClicked() {
        _signUpState.value =
            _signUpState.value.copy(isPasswordVisible = !_signUpState.value.isPasswordVisible)
    }

    private fun updateStateWithError(message: String) {
        _signUpState.value = _signUpState.value.copy(
            isPasswordError = true,
            passSuppText = message
        )
    }

    private fun resetPasswordError() {
        _signUpState.value = _signUpState.value.copy(
            isPasswordError = false,
            passSuppText = ""
        )
    }

    fun onEmailSignUpClicked(navigateToSignIn: () -> Unit) {
        val emailInput = _signUpState.value.email.trim()
        val passwordInput = _signUpState.value.password

        val errorMessage = validateSignUpInputs(emailInput, passwordInput)
        if (errorMessage != null) {
            updateStateWithError(errorMessage)
            return
        }

        signUpEmail(navigateToSignIn)
    }

    private fun validateSignUpInputs(email: String, password: String): String? {
        if (email.isEmpty() || password.isEmpty()) {
            return resourceProvider.getString(R.string.empty_fields)
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return resourceProvider.getString(R.string.invalid_email)
        }

        if (password.length < 9) {
            return resourceProvider.getString(R.string.weak_password)
        }

        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]+$".toRegex()
        if (!password.matches(passwordRegex)) {
            return resourceProvider.getString(R.string.password_requirements)
        }

        return null
    }

    fun signUpEmail(navigateToSignIn: () -> Unit) {
        viewModelScope.launch {

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    authService.signUp(_signUpState.value.email, _signUpState.value.password)
                }
            }

            result.fold(
                onSuccess = { user ->
                    if (user != null) {
                        navigateToSignIn()
                    }
                },
                onFailure = { exception ->
                    updateStateWithError(
                        exception.message ?: resourceProvider.getString(R.string.default_error)
                    )
                }
            )

        }
    }

}