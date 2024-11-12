package com.example.languageconverter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TranslationStartScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(modifier= Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 1f)))

        Image(painter = painterResource(id = R.drawable.langconv), contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f),
            contentScale = ContentScale.FillHeight,
            )

        Column(modifier= Modifier.fillMaxSize()) {
            Box(modifier= Modifier
                .fillMaxSize()
                .padding(40.dp),
                contentAlignment = Alignment.BottomCenter) {
                Button(
                    onClick = {
                        navController.navigate("")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(Color.White // Set button color to white
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
Text(text = "GET STARTED",
    modifier = Modifier,
    fontSize = 20.sp,
    color = Color(0xFFFFA500))
                }
            }
        }
    }


}

