package com.example.translator.ui


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.translator.R
import com.example.translator.model.TranslationViewModel

@Composable
fun TranslationScreen(viewModel: TranslationViewModel = viewModel()) {
    var textToTranslate by remember { mutableStateOf("") }
    val translationResult by viewModel.translationResult.collectAsState()
    val detectedLanguage by viewModel.detectedLanguage.collectAsState()

    var sourceLanguageExpanded by remember { mutableStateOf(false) }
    var targetLanguageExpanded by remember { mutableStateOf(false) }
    var selectedSourceLanguage by remember { mutableStateOf("hi") }  // Default to English
    var selectedTargetLanguage by remember { mutableStateOf("en") }
    var input by remember { mutableStateOf("Input language")}
    var output by remember { mutableStateOf("Output langauge") }
    if(selectedSourceLanguage=="hi"){
        input = "HINDI"
    }
    else if(selectedSourceLanguage=="en"){
        input= "ENGLISH"
    }
    else{
        input = "CHINESE"
    }
    if(selectedTargetLanguage=="hi"){
        output = "HINDI"
    }
    else if(selectedTargetLanguage=="en"){
        output= "ENGLISH"
    }
    else{
        output = "CHINESE"
    }
    // Default to Hindi
    val languages = mapOf("English" to "en", "Hindi" to "hi", "Mandarin" to "zh")
    var lang by remember { mutableStateOf("en-US") }
    // Obtain the current context
    val context = LocalContext.current
    val activity = context as? Activity
    var isRecognizing by remember { mutableStateOf(false) }
    // Text-to-Speech initialization based on the selected target language
    var textToSpeech: android.speech.tts.TextToSpeech? by remember { mutableStateOf(null) }
    LaunchedEffect(selectedTargetLanguage) {
        val locale = when (selectedTargetLanguage) {
            "en" -> java.util.Locale("en","US")
            "hi" -> java.util.Locale("hi", "IN") // Hindi Locale
            "zh" -> java.util.Locale.CHINESE
            else -> java.util.Locale("en","US") // Default case
        }
        Log.d(locale.toString(),"chek")

        textToSpeech = android.speech.tts.TextToSpeech(context) { status ->
            if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                textToSpeech?.setLanguage(locale)
            }
        }
    }

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    // Set RecognitionListener for SpeechRecognizer
    LaunchedEffect(speechRecognizer) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // Optionally, you can show a Toast or update UI when ready
            }

            override fun onBeginningOfSpeech() {
                // Optionally, update UI when speech begins
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Optionally, update UI to show speech level
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Optionally, handle buffer
            }

            override fun onEndOfSpeech() {
                // Optionally, update UI when speech ends
            }

            override fun onError(error: Int) {
                //Toast.makeText(context, "Speech recognition error: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    textToTranslate = matches[0]
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Optionally, handle partial results
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Optionally, handle events
            }
        })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF700201)) // Set background color to #700201
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center content vertically
    ) {
        Text(
            text = "Input Text",
            style = TextStyle(fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = textToTranslate,
            onValueChange = { textToTranslate = it },
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Yellow, shape = RoundedCornerShape(8.dp)) // Yellow background with rounded corners
                .padding(16.dp)
        )


        Spacer(modifier = Modifier.height(16.dp))

        LanguageDropdownMenu(
            label = input+" (Input Lang)",
            expanded = sourceLanguageExpanded,
            onExpandedChange = { sourceLanguageExpanded = it },
            selectedLanguage = selectedSourceLanguage,
            onLanguageSelected = {
                if (isRecognizing) {
                    isRecognizing=false
                    speechRecognizer.stopListening()

                }
                selectedSourceLanguage = it },
            languages = languages
        )

        Spacer(modifier = Modifier.height(16.dp))

        LanguageDropdownMenu(
            label = output+" (Output Lang)",
            expanded = targetLanguageExpanded,
            onExpandedChange = { targetLanguageExpanded = it },
            selectedLanguage = selectedTargetLanguage,
            onLanguageSelected = { selectedTargetLanguage = it },
            languages = languages
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Output Text",
            style = TextStyle(fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = translationResult.translatedText,
            onValueChange = {},
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Yellow, shape = RoundedCornerShape(8.dp)) // Yellow background with rounded corners
                .padding(16.dp),
            readOnly = true
        )

        // Speaker Icon for Text-to-Speech (Output Text)
        IconButton(
            onClick = {
                textToSpeech?.speak(translationResult.translatedText, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker), // Replace with your speaker icon
                contentDescription = "Speak Output Text",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.translate(textToTranslate, selectedSourceLanguage, selectedTargetLanguage)
            }
        ) {
            Text("Translate")
        }
    }
}

@Composable
fun LanguageDropdownMenu(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    languages: Map<String, String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp)) // White background with rounded corners
            .clickable { onExpandedChange(true) }
            .padding(16.dp)
    ) {
        Text(
            text = label,
            color = Color.Black
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            languages.forEach { (name, code) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onLanguageSelected(code)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
