package com.bittech.manga

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bittech.manga.ui.HomeScreen
import com.bittech.manga.ui.auth.LoginScreen
import com.bittech.manga.ui.auth.LoginViewModel
import com.bittech.manga.ui.theme.PupilMeshTheme
import com.bittech.manga.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PupilMeshTheme {
                val viewModel: LoginViewModel = hiltViewModel()
                val navController = rememberNavController()
                val signedIn = viewModel.uiState.value.isSignedIn


                NavHost(
                    navController = navController,
                    startDestination = if (signedIn) Routes.HOME else Routes.LOGIN
                ) {

                    //LoginScreen Route
                    composable(Routes.LOGIN) {
                        LoginScreen(onLoginSuccess = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true } //prevent going back to login
                            }
                        })
                    }

                    //HomeScreenRoute
                    composable(Routes.HOME) {
                        HomeScreen()
                    }

                    //DetailedScreenRoute

                    //FaceDetectionScreenRoute
                }
            }
        }
    }
}


