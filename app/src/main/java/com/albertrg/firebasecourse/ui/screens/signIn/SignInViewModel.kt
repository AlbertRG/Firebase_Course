package com.albertrg.firebasecourse.ui.screens.signIn

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

) : ViewModel() {

    private var _signInState = mutableStateOf(SignInState())
    val signInState: State<SignInState> get() = _signInState

}