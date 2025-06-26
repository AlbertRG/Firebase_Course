package com.albertrg.firebasecourse.data

import com.albertrg.firebasecourse.R
import com.albertrg.firebasecourse.utils.ResourceProvider
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val resourceProvider: ResourceProvider
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

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser? {
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

    private suspend fun completeSignUpWithCredential(credential: AuthCredential): FirebaseUser? {
        return runCatching {
            firebaseAuth.signInWithCredential(credential).await().user
        }.onFailure { exception ->
            when (exception) {
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

    fun getGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourceProvider.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(resourceProvider.getContext(), gso)
    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return completeSignUpWithCredential(credential)
    }

    suspend fun signInWithFacebook(accessToken: AccessToken): FirebaseUser? {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        return completeSignUpWithCredential(credential)
    }

}