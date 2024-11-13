package com.example.languageconverter

import android.app.Application
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavScreen(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "startScreen") {
        composable("startScreen"){
            TranslationStartScreen(navController)
        }
        composable("translatorScreen"){
            TranslationApp(TranslationViewModel(Application()), TextToSpeechViewModel(Application()))
        }

    }
}
