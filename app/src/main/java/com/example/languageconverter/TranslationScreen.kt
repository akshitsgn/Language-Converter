package com.example.languageconverter

import android.app.Application
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

@Composable
fun TranslationApp() {
    val viewModel= TranslationViewModel(Application())
    val modelStatus by viewModel.modelStatus
    val translatedText by viewModel.translatedText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Language Converter", style = MaterialTheme.typography.titleLarge)

        // Input field for text to be translated
        var inputText by remember { mutableStateOf(TextFieldValue("")) }

        BasicTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .padding(8.dp)
        )

        // Model status (downloading, ready, or error)
        Text(text = "Model Status: $modelStatus")

        // Button to trigger translation
        Button(
            onClick = { viewModel.translateText(inputText.text) },
            enabled = modelStatus == "Model ready for translation",
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Translate")
        }

        // Display the translated text
        Text(
            text = "Translated Text:",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = translatedText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .padding(8.dp)
        )
    }
}