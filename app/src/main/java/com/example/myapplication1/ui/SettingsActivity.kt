package com.example.myapplication1.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var providerSpinner: Spinner
    private lateinit var geminiSettingsLayout: LinearLayout
    private lateinit var apiKeyEditText: EditText
    private lateinit var saveApiKeyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Find all the views from our XML layout
        providerSpinner = findViewById(R.id.providerSpinner)
        geminiSettingsLayout = findViewById(R.id.geminiSettingsLayout)
        apiKeyEditText = findViewById(R.id.apiKeyEditText)
        saveApiKeyButton = findViewById(R.id.saveApiKeyButton)

        setupProviderSpinner()
        loadSettings()

        saveApiKeyButton.setOnClickListener {
            saveApiKey()
        }
    }

    private fun setupProviderSpinner() {
        // Create an adapter for our provider choices
        val providers = arrayOf("G4F (Placeholder)", "Gemini API")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, providers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        providerSpinner.adapter = adapter

        // Set a listener to react when the user selects a provider
        providerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedProvider = providers[position]
                if (selectedProvider == "Gemini API") {
                    geminiSettingsLayout.visibility = View.VISIBLE
                } else {
                    geminiSettingsLayout.visibility = View.GONE
                }
                // Save the selected provider choice
                saveProviderChoice(selectedProvider)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveProviderChoice(provider: String) {
        val sharedPreferences = getSharedPreferences("AliceSettings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("SELECTED_PROVIDER", provider).apply()
    }

    private fun saveApiKey() {
        val apiKey = apiKeyEditText.text.toString().trim()
        if (apiKey.isNotEmpty()) {
            val sharedPreferences = getSharedPreferences("AliceSettings", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("GEMINI_API_KEY", apiKey).apply()
            Toast.makeText(this, "API Key saved!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "API Key cannot be empty.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("AliceSettings", Context.MODE_PRIVATE)
        
        // Load the saved API key
        val savedApiKey = sharedPreferences.getString("GEMINI_API_KEY", "")
        apiKeyEditText.setText(savedApiKey)

        // Load and set the saved provider
        val savedProvider = sharedPreferences.getString("SELECTED_PROVIDER", "G4F (Placeholder)")
        val providerPosition = (providerSpinner.adapter as ArrayAdapter<String>).getPosition(savedProvider)
        providerSpinner.setSelection(providerPosition)
    }
}