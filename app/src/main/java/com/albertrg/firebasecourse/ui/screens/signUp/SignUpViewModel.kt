package com.albertrg.firebasecourse.ui.screens.signUp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : ViewModel() {

    private var _signUpState = mutableStateOf(SignUpState())
    val signUpState: State<SignUpState> get() = _signUpState

}