// DataBarcode.kt
package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.os.Handler

class Databarcode: AppCompatActivity() {
    private lateinit var timerTextView: TextView
    private val handler = Handler()
    private var seconds = 0

    private val runnable = object : Runnable {
        override fun run() {
            seconds++
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            timerTextView.text = String.format("%02d:%02d", minutes, remainingSeconds) // Display minutes and seconds
            handler.postDelayed(this, 1000) // Update every second
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_databarcode)

        // Get the data passed from the previous activity
        val displayText = intent.getStringExtra("DISPLAY_TEXT")

        displayText?.let{
            val data = it.split(";")

            if(data.size == 3){
                val dataNameView = findViewById<TextView>(R.id.dataName) // Example data array
                dataNameView.text = data[2]

                val idNameView = findViewById<TextView>(R.id.dataID)
                idNameView.text = data[1]
            }
        }

        val reScanButton: Button = findViewById(R.id.rescanBtn)
        reScanButton.setOnClickListener {

            val scannerOpener = Intent(this, ScannerActivity::class.java)
            startActivity(scannerOpener)
        }

        val conFirmButton: Button = findViewById(R.id.confirmBtn)
        conFirmButton.setOnClickListener{

            reScanButton.isEnabled = false
            conFirmButton.isEnabled = false


            val blinkingAnimation = ContextCompat.getDrawable(this, R.drawable.blinking_animation) as AnimationDrawable

            conFirmButton.background = blinkingAnimation
            blinkingAnimation.start()


            conFirmButton.text = if (conFirmButton.isEnabled) {
                ""
            } else {
                "REF OPEN"
            }


            timerTextView = findViewById(R.id.timerDisplay)
            handler.post(runnable)


        }



        /*

        */

    }



    override fun onBackPressed() {
        // Do nothing to disable back button
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Stop the timer when activity is destroyed
    }
}




