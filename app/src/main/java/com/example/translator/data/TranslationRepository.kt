package com.example.translator.data

import com.example.langconverter.data.TranslationService
import com.example.langconverter.model.TranslationResult
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await


class TranslationRepository : TranslationService {

    override suspend fun translateText(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): TranslationResult {

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        // Initialize the ML Kit translator
        val translator = Translation.getClient(options)
        return try {
            // Download language models if necessary
            translator.downloadModelIfNeeded().await()

            // Perform the translation
            val translatedText = translator.translate(text).await()

            // Return the result as successful
            TranslationResult(translatedText)
        } catch (e: Exception) {
            // Handle any errors during translation
            TranslationResult("Error: ${e.localizedMessage}")
        } finally {
            // Close the translator to release resources
            translator.close()
        }
    }
}
