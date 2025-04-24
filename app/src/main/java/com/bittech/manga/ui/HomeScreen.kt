package com.bittech.manga.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bittech.manga.ui.face.FaceRecognitionScreen
import com.bittech.manga.ui.manga.MangaScreen
import com.bittech.manga.ui.manga.MangeDetailScreen
import com.bittech.manga.ui.theme.PupilMeshTheme
import com.bittech.manga.utils.BottomNavItems
import com.bittech.manga.utils.Routes

@Composable
fun HomeScreen() {
    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavItems.Manga.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItems.Manga.route) {
                MangaScreen(navController=navController)
            }
            composable(BottomNavItems.FaceRecognition.route) {
                FaceRecognitionScreen()
            }

            composable(
                route = Routes.MANGA_DETAILED+"/{mangaId}",
                arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
            ) { backStackEntry ->
                val mangaId = backStackEntry.arguments?.getString("mangaId")
                mangaId?.let {
                    MangeDetailScreen(
                        mangaId = it
                    )
                }
            }
        }
    }


}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItems.Manga,
        BottomNavItems.FaceRecognition
    )
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewHomeScreen() {
    PupilMeshTheme {
        HomeScreen()
    }
}