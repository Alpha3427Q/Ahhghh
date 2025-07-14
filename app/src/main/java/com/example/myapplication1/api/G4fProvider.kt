package com.example.myapplication1.api

import com.example.myapplication1.adapter.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A placeholder provider for G4F.
 * It doesn't connect to a real service yet, it just returns a canned response.
 */
class G4fProvider : AiProvider {
    override fun sendMessage(
        message: String,
        history: List<Message>,
        onResponse: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Simulate a network delay
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500) // Wait for 1.5 seconds
            onResponse("This is a placeholder response from the G4F provider. The real connection will be built later.")
        }
    }
}