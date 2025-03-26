package com.example.translator.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.data.TranslationRepository
import com.example.langconverter.model.TranslationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TranslationViewModel : ViewModel() {

    private val repository = TranslationRepository()

    private val _translationResult = MutableStateFlow(TranslationResult(""))
    val translationResult: StateFlow<TranslationResult> get() = _translationResult

    private val _detectedLanguage = MutableStateFlow("")
    val detectedLanguage: StateFlow<String> get() = _detectedLanguage

    fun translate(text: String, sourceLanguage: String, targetLanguage: String) {
        viewModelScope.launch {
            val result = repository.translateText(text, sourceLanguage, targetLanguage)
            _translationResult.value = result
        }
    }
}
