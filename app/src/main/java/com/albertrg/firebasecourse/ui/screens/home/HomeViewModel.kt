package com.albertrg.firebasecourse.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertrg.firebasecourse.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    fun signOut(navigateToSignIn: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            authService.signOut()
        }
        navigateToSignIn()
    }

}