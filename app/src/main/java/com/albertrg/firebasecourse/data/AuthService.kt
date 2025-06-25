package com.albertrg.firebasecourse.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun login(email: String, password: String): FirebaseUser? {
        return runCatching {
            firebaseAuth.signInWithEmailAndPassword(email, password).await().user
        }.onFailure { exception ->
            when (exception) {
                is FirebaseAuthInvalidUserException -> {
                    throw Exception("User does not exist. Please check your email.")
                }

                is FirebaseAuthInvalidCredentialsException -> {
                    throw Exception("Invalid email or password. Please try again.")
                }

                else -> {
                    throw Exception("An unexpected error occurred. Please try again later.")
                }
            }
        }.getOrThrow()
    }

}