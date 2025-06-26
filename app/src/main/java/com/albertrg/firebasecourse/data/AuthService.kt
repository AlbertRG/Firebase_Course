package com.albertrg.firebasecourse.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    private fun getCurrentUser() = firebaseAuth.currentUser

    suspend fun signUp(email: String, password: String): FirebaseUser? {
        return runCatching {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
        }.onFailure { exception ->
            when (exception) {
                is FirebaseAuthWeakPasswordException -> {
                    throw Exception("The given password is too weak. Please use a stronger password.")
                }

                is FirebaseAuthInvalidCredentialsException -> {
                    throw Exception("The email address is badly formatted. Please enter a valid email.")
                }

                is FirebaseAuthUserCollisionException -> {
                    throw Exception("The email address is already in use by another account.")
                }

                is FirebaseNetworkException -> {
                    throw Exception("A network error occurred. Please check your internet connection.")
                }

                else -> {
                    throw Exception("An unexpected error occurred. Please try again later.")
                }
            }
        }.getOrThrow()
    }

    suspend fun signIn(email: String, password: String): FirebaseUser? {
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

                is FirebaseNetworkException -> {
                    throw Exception("A network error occurred. Please check your internet connection.")
                }

                else -> {
                    throw Exception("An unexpected error occurred. Please try again later.")
                }
            }
        }.getOrThrow()
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun isUserSignedIn(): Boolean {
        return getCurrentUser() != null
    }

}