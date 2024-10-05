package com.example.myapplication

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var openScannerButton: Button
    private lateinit var refrigeratorNumberTextView: TextView
    private val wifiReceiver = WifiReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.openScanner)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        openScannerButton = findViewById(R.id.openScanner)
        refrigeratorNumberTextView = findViewById(R.id.refrigeratorNumberTextView)

        // Check for WRITE_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        checkWifiConnection()

        openScannerButton.setOnClickListener {
            if (openScannerButton.isEnabled) {
                val refrigeratorNumber = refrigeratorNumberTextView.text.toString()
                val scannerOpener = Intent(this, ScannerActivity::class.java)
                scannerOpener.putExtra("REFRIGERATOR_NUMBER", refrigeratorNumber)
                startActivity(scannerOpener)
            } else {
                Toast.makeText(this, "Please connect to Wi-Fi to proceed.", Toast.LENGTH_SHORT).show()
            }
        }

        // Manage refrigerator number and update the TextView
        val refrigeratorNumber = manageRefrigeratorFile()
        refrigeratorNumberTextView.text = "Refrigerator Number: $refrigeratorNumber"
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiReceiver)
    }

    private fun checkWifiConnection() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            val isWiFiConnected = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

            openScannerButton.isEnabled = isWiFiConnected

            if (!isWiFiConnected) {
                Toast.makeText(this, "Wi-Fi is not connected. Please connect to Wi-Fi.", Toast.LENGTH_SHORT).show()
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            val isWiFiConnected = activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI

            openScannerButton.isEnabled = isWiFiConnected

            if (!isWiFiConnected) {
                Toast.makeText(this, "Wi-Fi is not connected. Please connect to Wi-Fi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun manageRefrigeratorFile(): String {
        val directory = File(filesDir, "Refrigerator") // Change "Gg" to the desired directory
        if (!directory.exists()) {
            directory.mkdirs() // Create the directory if it doesn't exist
        }

        val fileName = "refrigerator number.txt"
        val file = File(directory, fileName) // Update file path to use the new directory

        Log.d("FilePath", "Attempting to access: ${file.absolutePath}")

        return if (file.exists()) {
            Log.d("FileCheck", "File exists, reading number.")
            readRefrigeratorNumber(file)
        } else {
            Log.d("FileCheck", "File does not exist, writing default number.")
            return if (writeRefrigeratorNumber(file, "REF001")) {
                "REF001" // Return the default number only if writing is successful
            } else {
                "Error creating refrigerator number"
            }
        }
    }


    private fun writeRefrigeratorNumber(file: File, number: String): Boolean {
        return try {
            FileOutputStream(file).use { output ->
                output.write(number.toByteArray())
            }
            true // Writing was successful
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing to file", Toast.LENGTH_SHORT).show()
            false // Writing failed
        }
    }


    private fun readRefrigeratorNumber(file: File): String {
        return try {
            FileInputStream(file).use { input ->
                input.bufferedReader().use { reader ->
                    reader.readLine() ?: "No number found"
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error reading from file"
        }
    }

    private inner class WifiReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkWifiConnection()
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}