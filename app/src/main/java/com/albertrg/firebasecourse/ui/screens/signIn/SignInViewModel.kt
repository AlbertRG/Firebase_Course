package com.albertrg.firebasecourse.ui.screens.signIn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertrg.firebasecourse.R
import com.albertrg.firebasecourse.data.AuthService
import com.albertrg.firebasecourse.utils.ResourceProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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
class SignInViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val authService: AuthService
) : ViewModel() {

    private var _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> =
        _signInState.onStart {
            //init
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInState()
        )

    fun onEmailChanged(uiEmail: String) {
        _signInState.value = _signInState.value.copy(email = uiEmail)
    }

    fun onPasswordChanged(uiPassword: String) {
        _signInState.value = _signInState.value.copy(password = uiPassword)
        resetPasswordError()
    }


    fun onPasswordVisibilityClicked() {
        _signInState.value =
            _signInState.value.copy(isPasswordVisible = !_signInState.value.isPasswordVisible)
    }

    private fun updateStateWithError(message: String) {
        _signInState.value = _signInState.value.copy(
            isPasswordError = true,
            passSuppText = message
        )
    }

    private fun resetPasswordError() {
        _signInState.value = _signInState.value.copy(
            isPasswordError = false,
            passSuppText = ""
        )
    }

    fun onEmailSignInClicked(navigateToHome: () -> Unit) {
        val emailInput = _signInState.value.email.trim()
        val passwordInput = _signInState.value.password

        val errorMessage = validateSignInInputs(emailInput, passwordInput)
        if (errorMessage != null) {
            updateStateWithError(errorMessage)
            return
        }

        signInWithEmail(navigateToHome)
    }

    private fun validateSignInInputs(email: String, password: String): String? {
        if (email.isEmpty() || password.isEmpty()) {
            return resourceProvider.getString(R.string.empty_fields)
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return resourceProvider.getString(R.string.invalid_email)
        }

        return null
    }

    fun signInWithEmail(navigateToHome: () -> Unit) {
        viewModelScope.launch {

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    authService.signInWithEmailAndPassword(
                        _signInState.value.email,
                        _signInState.value.password
                    )
                }
            }

            result.fold(
                onSuccess = { user ->
                    if (user != null) {
                        navigateToHome()
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

    fun onGoogleClicked(googleLauncherSignIn: (GoogleSignInClient) -> Unit) {
        val gsc = authService.getGoogleClient()
        googleLauncherSignIn(gsc)
    }

    fun signInWithGoogle(idToken: String, navigateToHome: () -> Unit) {
        Log.d("SignInViewModel", "signInWithGoogle: $idToken")
        viewModelScope.launch {

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    authService.signInWithGoogle(idToken)
                }
            }

            result.fold(
                onSuccess = { user ->
                    if (user != null) {
                        navigateToHome()
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