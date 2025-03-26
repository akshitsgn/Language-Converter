package com.example.translator.translation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.translator.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }

    // URI for the video file
    val videoUri =
        Uri.parse("android.resource://${context.packageName}/raw/splash_screen") // Replace with your video file

    // Prepare and play the video
    LaunchedEffect(true) {
        // Create MediaItem
        val mediaItem = MediaItem.Builder().setUri(videoUri).build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        // Wait for the video duration (or set a fixed delay)
        delay(3600) // Adjust the duration (in milliseconds) for the splash screen

        // Navigate to the main screen after the splash
        navController.navigate("main") // Modify as per your screen navigation
    }
    Column(modifier=Modifier.fillMaxSize()){
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF5e0606))){
            Image(
                painter = painterResource(id = R.drawable.bg), // Reference to your background image (bg.png)
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize() // Make the image take full screen
            )
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        this.player = player

                        // Disable controls to make it act like an animation (no buttons)
                        useController = false // No play/pause/seek buttons
                    }
                },
                modifier = Modifier.fillMaxSize() // Make the PlayerView occupy the full screen
            )
        }
    }
}
