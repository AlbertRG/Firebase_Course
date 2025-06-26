package com.albertrg.firebasecourse.ui.screens.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.albertrg.firebasecourse.R
import com.albertrg.firebasecourse.ui.composables.SignBackground

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel,
    navigateToSignIn: () -> Unit
) {
    val signUpState by signUpViewModel.signUpState.collectAsStateWithLifecycle()

    SignBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
                text = "Sign Up",
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
                value = signUpState.email,
                onValueChange = { signUpViewModel.onEmailChanged(it) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                value = signUpState.password,
                onValueChange = {
                    if (it.length <= 18) {
                        signUpViewModel.onPasswordChanged(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = {
                    Text("Password")
                },
                trailingIcon = {
                    val toggle = if (signUpState.isPasswordVisible)
                        R.drawable.baseline_visibility_24
                    else
                        R.drawable.baseline_visibility_off_24
                    IconButton(
                        onClick = {
                            signUpViewModel.onPasswordVisibilityClicked()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = toggle),
                            contentDescription = if (signUpState.isPasswordVisible) "Hide password"
                            else "Show password",
                            tint = Color.White
                        )
                    }
                },
                supportingText = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                    ) {
                        Text(
                            signUpState.passSuppText,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)

                        )
                        Text(
                            modifier = Modifier.width(35.dp),
                            text = "${signUpState.password.length}/18",
                            color = Color.White
                        )
                    }
                },
                visualTransformation = if (signUpState.isPasswordVisible) VisualTransformation.None
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
            Button(
                onClick = {
                    signUpViewModel.onEmailSignUpClicked(navigateToSignIn)
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
                    text = "Sign Up",
                    fontSize = 16.sp,
                )
            }
        }
    }

}