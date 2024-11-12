package com.example.languageconverter

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TranslationApp(viewModel: TranslationViewModel, viewModel1: TextToSpeechViewModel) {

    val modelStatus by viewModel.modelStatus.collectAsState()
    val translatedText by viewModel.translatedText
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel1.initializeTextToSpeech(context)
    }

    Text(text = "Language Converter", style = MaterialTheme.typography.titleLarge)


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

    Button(
        onClick = { viewModel.translateText(inputText.text) },
        enabled = modelStatus == "Model ready for translation",
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Translate")
    }

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
    Button(onClick = {
        viewModel1.speak(translatedText)
    }) {
        Text("Speak")
    }
}