package com.example.languageconverter

import android.app.Application
import android.speech.tts.TextToSpeech
import android.view.translation.Translator
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class TranslationViewModel(application: Application) : ViewModel(){

    private var translator: com.google.mlkit.nl.translate.Translator? = null
    private val _translatedText = mutableStateOf("")
    val translatedText = _translatedText

    val modelStatus = MutableStateFlow("Initializing...")  // To track download status and errors
    var inputText = ""

    private var textToSpeech: TextToSpeech? = null
    private val _isTtsReady = mutableStateOf(false)
    val isTtsReady = _isTtsReady

    init {
        setupTranslator()
    }

    private fun setupTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()

        translator = Translation.getClient(options)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { modelStatus.value = "Downloading model..." }
                downloadTranslationModel()
                withContext(Dispatchers.Main) { modelStatus.value = "Model ready for translation" }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    modelStatus.value = "Error downloading model: ${e.localizedMessage}"
                }
            }
        }
    }

    private suspend fun downloadTranslationModel() {
        val conditions = DownloadConditions.Builder().build()
        suspendCancellableCoroutine<Unit> { continuation ->
            translator?.downloadModelIfNeeded(conditions)
                ?.addOnSuccessListener {
                    continuation.resume(Unit)
                }
                ?.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    fun translateText(inputText: String) {
        if (modelStatus.value == "Model ready for translation") {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) { _translatedText.value = "Translating..." }
                    val translated = translate(inputText)
                    withContext(Dispatchers.Main) { _translatedText.value = translated }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _translatedText.value = "Translation failed: ${e.localizedMessage}"
                    }
                }
            }
        } else {
            _translatedText.value = modelStatus.value
        }
    }

    private suspend fun translate(inputText: String): String {
        return suspendCancellableCoroutine { continuation ->
            translator?.translate(inputText)
                ?.addOnSuccessListener { translatedText ->
                    continuation.resume(translatedText)
                }
                ?.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    fun cleanup() {
        translator?.close()
        _translatedText.value = ""
        modelStatus.value = "Initializing..."
        setupTranslator()
    }

    override fun onCleared() {
        super.onCleared()
        translator?.close()
    }
}
