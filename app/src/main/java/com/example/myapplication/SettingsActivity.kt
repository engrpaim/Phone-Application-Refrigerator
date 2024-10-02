package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var urlEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        urlEditText = findViewById(R.id.urlEditText)
        saveButton = findViewById(R.id.saveButton)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Load the saved URL
        val savedUrl = sharedPreferences.getString("BASE_URL", "http://172.17.8.60")
        urlEditText.setText(savedUrl)

        saveButton.setOnClickListener {
            val newUrl = urlEditText.text.toString()
            sharedPreferences.edit().putString("BASE_URL", newUrl).apply()
            finish() // Close the settings activity
        }
    }
}
