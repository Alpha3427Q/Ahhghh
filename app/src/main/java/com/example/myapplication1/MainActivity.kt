package com.example.myapplication1

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.FontAssetDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication1.adapter.ChatAdapter
import com.example.myapplication1.adapter.Message
import com.example.myapplication1.api.AiProvider
import com.example.myapplication1.api.G4fProvider
import com.example.myapplication1.api.GeminiProvider

class MainActivity : AppCompatActivity() {

    // --- UI Elements ---
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var userInputEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var settingsButton: ImageButton // New UI Element

    // --- Chat Logic ---
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<Message>()
    private var aiProvider: AiProvider? = null // To hold our selected AI provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setContentView(R.layout.activity_main)
        initializeViews()
        setupLottieFontHandler()
        setupRecyclerView()
        setupClickListeners()
        addMessageToChat("Hello, I am Alice. How can I help you today?", Message.SENDER_ALICE)
    }

    override fun onResume() {
        super.onResume()
        setupAiProvider() // Ensure settings are refreshed
    }

    private fun setupWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
    }

    private fun initializeViews() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        userInputEditText = findViewById(R.id.userInputEditText)
        sendButton = findViewById(R.id.sendButton)
        lottieAnimationView = findViewById(R.id.jarvis_animation_view)
        settingsButton = findViewById(R.id.settingsButton)
    }

    private fun setupClickListeners() {
        // Handle Send Button Click
        sendButton.setOnClickListener {
            val userMessage = userInputEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                sendMessage(userMessage)
            } else {
                Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Handle Settings Button Click (fixed intent)
        settingsButton.setOnClickListener {
            Toast.makeText(this, "Opening Settings...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, com.example.myapplication1.ui.SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAiProvider() {
        val sharedPreferences = getSharedPreferences("AliceSettings", Context.MODE_PRIVATE)
        val selectedProvider = sharedPreferences.getString("SELECTED_PROVIDER", "G4F (Placeholder)")
        val apiKey = sharedPreferences.getString("GEMINI_API_KEY", "")

        aiProvider = if (selectedProvider == "Gemini API" && !apiKey.isNullOrEmpty()) {
            GeminiProvider(apiKey)
        } else {
            if (selectedProvider == "Gemini API") {
                Toast.makeText(this, "Gemini API key not set. Using placeholder.", Toast.LENGTH_LONG).show()
            }
            G4fProvider()
        }
    }

    private fun setupLottieFontHandler() {
        val fontAssetDelegate = object : FontAssetDelegate() {
            override fun fetchFont(fontFamily: String?): Typeface {
                return Typeface.createFromAsset(assets, "fonts/Roboto-Bold.ttf")
            }
        }
        lottieAnimationView.setFontAssetDelegate(fontAssetDelegate)
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messageList)
        val layoutManager = LinearLayoutManager(this)
        chatRecyclerView.layoutManager = layoutManager
        chatRecyclerView.adapter = chatAdapter
    }

    private fun sendMessage(userMessage: String) {
        addMessageToChat(userMessage, Message.SENDER_USER)
        userInputEditText.setText("")

        val thinkingMessage = "Thinking..."
        addMessageToChat(thinkingMessage, Message.SENDER_ALICE)
        val thinkingMessagePosition = messageList.size - 1

        aiProvider?.sendMessage(
            message = userMessage,
            history = messageList.dropLast(1),
            onResponse = { aiResponse ->
                updateMessageInChat(aiResponse, thinkingMessagePosition)
            },
            onError = { errorMessage ->
                updateMessageInChat("Error: $errorMessage", thinkingMessagePosition)
            }
        )
    }

    private fun addMessageToChat(text: String, sender: String) {
        val message = Message(text, sender)
        messageList.add(message)
        chatAdapter.notifyItemInserted(messageList.size - 1)
        chatRecyclerView.scrollToPosition(messageList.size - 1)
    }

    private fun updateMessageInChat(newText: String, position: Int) {
        if (position in messageList.indices) {
            val updatedMessage = messageList[position].copy(text = newText)
            messageList[position] = updatedMessage
            chatAdapter.notifyItemChanged(position)
        }
    }
}