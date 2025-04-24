package com.bittech.manga.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItems(val route: String, val label: String, val icon: ImageVector) {
    object Manga : BottomNavItems("manga", "Manga", Icons.Default.Home)
    object FaceRecognition : BottomNavItems("profile", "Profile", Icons.Default.AccountCircle)

}