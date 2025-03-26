package com.example.translator


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.translator.translation.SplashScreen
import com.example.translator.ui.TranslationScreen


@Composable
fun NavScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("main") {
            TranslationScreen()
        }
    }
}