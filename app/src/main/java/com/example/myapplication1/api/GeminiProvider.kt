package com.example.myapplication1.api

import com.example.myapplication1.adapter.Message
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeminiProvider(
    apiKey: String,
    modelName: String = "gemini-1.5-flash-latest"
) : AiProvider {

    private val personaPrompt = "You are a helpful, intelligent AI assistant. Your name is Alice. Your creator's name is Alpha. Always maintain this persona."

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey,
        systemInstruction = content { text(personaPrompt) }
    )

    override fun sendMessage(
        message: String,
        history: List<Message>,
        onResponse: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val chatHistory = history.map { msg ->
                    content(role = if (msg.sender == Message.SENDER_USER) "user" else "model") {
                        text(msg.text)
                    }
                }

                val chat = generativeModel.startChat(history = chatHistory)
                val response = chat.sendMessage(message)

                CoroutineScope(Dispatchers.Main).launch {
                    onResponse(response.text ?: "Sorry, I received an empty response.")
                }

            } catch (e: Exception) {
                // Providing a more user-friendly error message
                CoroutineScope(Dispatchers.Main).launch {
                    val friendlyError = e.localizedMessage ?: "An unknown error occurred with the API."
                    onError(friendlyError)
                }
            }
        }
    }
}
