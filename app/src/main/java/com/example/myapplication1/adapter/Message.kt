package com.example.myapplication1.adapter

/**
 * A simple data class to hold the information for a single chat message.
 *
 * @property text The content of the message.
 * @property sender A String indicating who sent the message ("USER" or "ALICE").
 */
data class Message(
    val text: String,
    val sender: String
) {
    // Companion object to hold constants for sender types.
    // This prevents typos in our code later.
    companion object {
        const val SENDER_USER = "USER"
        const val SENDER_ALICE = "ALICE"
    }
}