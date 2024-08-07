// DataBarcode.kt
package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Databarcode: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_databarcode)

        // Get the data passed from the previous activity
        val displayText = intent.getStringExtra("DISPLAY_TEXT")

        displayText?.let{
            val data = it.split(";")

            if(data.size == 3){
                val dataNameView = findViewById<TextView>(R.id.dataName) // Example data array
                dataNameView.text = data[1]

                val idNameView = findViewById<TextView>(R.id.dataID)
                idNameView.text = data[2]
            }
        }

        /*

        */

    }

    override fun onBackPressed() {
        // Do nothing to disable back button
    }
}

fun mapData(value: String): String {
    return when (value) {
        "00" -> "Operator"
        "01" -> "Shift leader"
        else -> "Unknown"
    }
}