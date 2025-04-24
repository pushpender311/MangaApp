package com.bittech.manga.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bittech.manga.ui.theme.PupilMeshTheme
import com.bittech.pupilmesh.R

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        color = Color.DarkGray
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F1F1F),
                    contentColor = Color.White,
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Zenithra",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Welcome Back",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please enter your details to sign in",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = "Google Sign in",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(BorderStroke(1.dp, Color.DarkGray), shape = CircleShape)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_apple),
                                contentDescription = "Apple Sign in",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(BorderStroke(1.dp, Color.DarkGray), shape = CircleShape)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "OR",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        label = { Text(text = "Your Email Address", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color.White,
                            errorBorderColor = Color.Red,
                            errorTextColor = Color.Red,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val icon = if (passwordVisible) {
                        painterResource(R.drawable.ic_hide_password)
                    } else {
                        painterResource(R.drawable.ic_view_password)
                    }

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text(text = "Password", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = icon,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = Color.Gray
                                )
                            }
                        },

                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color.White,
                            errorBorderColor = Color.Red,
                            errorTextColor = Color.Red,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFF5B9BFF),
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { /*Navigate to forgot password*/ }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.loginOrRegister { success ->
                                if (success) onLoginSuccess()
                            }
                        }, //Navigate to home
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.DarkGray,
                            disabledContentColor = Color.Gray,
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Text("Sign In")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            "Don't have an account? ", color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            "Sign Up",
                            color = Color(0xFF5B9BFF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { /* navigate to signup */ }
                        )
                    }
                }
            }

        }

    }
}

@Composable
@Preview
fun PreviewLoginScreen() {
    PupilMeshTheme {
        LoginScreen({})
    }
}