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
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Databarcode : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private val handler = Handler()
    private var seconds = 0
    private var data1: String? = null
    private var data0: String? = null

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
        val currentTime: String = LocalTime.now()
            .format(DateTimeFormatter.ofPattern("HH:mm:ss"))




        val displayText = intent.getStringExtra("DISPLAY_TEXT")

        displayText?.let {
            val data = it.split(";")

            if (data.size == 3) {
                val dataNameView = findViewById<TextView>(R.id.dataName)
                dataNameView.text = data[2]

                val idNameView = findViewById<TextView>(R.id.dataID)
                idNameView.text = data[1]

                data1 = data[1] // Save data1
                data0 = data[2] // Save data0
            }
        }

        //region RESCAN PRESSED
        val reScanButton: Button = findViewById(R.id.rescanBtn)
        reScanButton.setOnClickListener {
            val scannerOpener = Intent(this, ScannerActivity::class.java)
            startActivity(scannerOpener)
        }
        //endregion

        //region CONFIRM PRESSED
        val conFirmButton: Button = findViewById(R.id.confirmBtn)
        conFirmButton.setOnClickListener {
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
        //endregion

        // Send data to server
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                val url = URL("http://172.17.8.60/refx/server.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")


                val content = "param0=$data0&param1=$data1&time=$currentTime"

                connection.outputStream.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }

                val responseCode = connection.responseCode
                val inputStream = connection.inputStream
                val responseText = inputStream.bufferedReader().use { it.readText() }
                connection.disconnect()

                if (responseCode == HttpURLConnection.HTTP_OK) responseText else "Error: $responseCode"
            } catch (e: Exception) {
                Log.e("NetworkTest", "Exception occurred", e)
                "Exception: ${e.message}"
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(this@Databarcode, response, Toast.LENGTH_LONG).show()
                Log.d("NetworkTest", response)
            }

        }
    }





    override fun onBackPressed() {
        val text = "Close the refrigerator first"
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(this, text, duration).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Stop the timer when activity is destroyed
    }
}
