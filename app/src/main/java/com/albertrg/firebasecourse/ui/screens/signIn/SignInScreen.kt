package com.albertrg.firebasecourse.ui.screens.signIn

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.albertrg.firebasecourse.R
import com.albertrg.firebasecourse.ui.composables.SignBackground
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    val signInState by signInViewModel.signInState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleOwner.lifecycleScope.launch {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        signInViewModel.signInWithGoogle(account.idToken!!) {
                            navigateToHome()
                        }
                    } catch (e: ApiException) {
                        print(e)
                    }
                }
            }
        }
    )

    SignBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_firebase),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(48.dp)
                )
            }
            Text(
                text = "Sign In",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.White,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            OutlinedTextField(
                value = signInState.email,
                onValueChange = { signInViewModel.onEmailChanged(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text("Email")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email",
                        tint = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            OutlinedTextField(
                value = signInState.password,
                onValueChange = {
                    if (it.length <= 18) {
                        signInViewModel.onPasswordChanged(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = {
                    Text("Password")
                },
                trailingIcon = {
                    val toggle = if (signInState.isPasswordVisible)
                        R.drawable.baseline_visibility_24
                    else
                        R.drawable.baseline_visibility_off_24
                    IconButton(
                        onClick = {
                            signInViewModel.onPasswordVisibilityClicked()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = toggle),
                            contentDescription = if (signInState.isPasswordVisible) "Hide password"
                            else "Show password",
                            tint = Color.White
                        )
                    }
                },
                supportingText = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            signInState.passSuppText,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)

                        )
                        Text(
                            modifier = Modifier.width(35.dp),
                            text = "${signInState.password.length}/18",
                            color = Color.White
                        )
                    }
                },
                visualTransformation = if (signInState.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedSupportingTextColor = Color.White,
                    unfocusedSupportingTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign Up",
                    modifier = Modifier
                        .clickable {
                            navigateToSignUp()
                        },
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .clickable {
                            /*TODO*/
                        },
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = {
                    signInViewModel.onEmailSignInClicked(navigateToHome)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFFC9304)
                )
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0f),
                                    Color.White.copy(alpha = 1f)
                                )
                            )
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Or continue with",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 1f),
                                    Color.White.copy(alpha = 0f)
                                )
                            )
                        )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        signInViewModel.onGoogleClicked { signInClient ->
                            val signInIntent = signInClient.signInIntent
                            googleLauncher.launch(signInIntent)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                    Text(
                        text = "Google",
                        color = Color.Black,
                    )
                }
            }
        }
    }

}