package com.albertrg.firebasecourse.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertrg.firebasecourse.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private var _splashState = MutableStateFlow(SplashState())
    val splashState: StateFlow<SplashState> =
        _splashState.onStart {
            determineNavigation()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SplashState()
        )

    private fun isUserSignedIn(): Boolean {
        return authService.isUserSignedIn()
    }

    fun determineNavigation() {
        viewModelScope.launch {
            delay(5000L)
            _splashState.value = _splashState.value.copy(
                isSignedIn = isUserSignedIn()
            )
        }
    }

}