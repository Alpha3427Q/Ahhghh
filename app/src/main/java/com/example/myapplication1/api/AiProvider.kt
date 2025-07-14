package com.example.myapplication1.api

/**
 * An interface that defines the contract for any AI service provider.
 * This allows us to easily switch between Gemini, G4F, or any other AI in the future.
 */
interface AiProvider {
    /**
     * Sends a message to the AI and gets a response.
     * @param message The user's text message.
     * @param history The list of previous messages for context.
     * @param onResponse A callback function that is triggered with the AI's full response.
     * @param onError A callback function that is triggered if an error occurs.
     */
    fun sendMessage(
        message: String,
        history: List<com.example.myapplication1.adapter.Message>,
        onResponse: (String) -> Unit,
        onError: (String) -> Unit
    )
}