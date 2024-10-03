package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var openScannerButton: Button
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
        checkWifiConnection()

        openScannerButton.setOnClickListener {
            if (openScannerButton.isEnabled) {
                val scannerOpener = Intent(this, ScannerActivity::class.java)
                startActivity(scannerOpener)
            } else {
                Toast.makeText(this, "Please connect to Wi-Fi to proceed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Register the broadcast receiver for connectivity changes
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        // Unregister the broadcast receiver to avoid memory leaks
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
            // For devices below API level 23
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            val isWiFiConnected = activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI

            openScannerButton.isEnabled = isWiFiConnected

            if (!isWiFiConnected) {
                Toast.makeText(this, "Wi-Fi is not connected. Please connect to Wi-Fi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class WifiReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Check Wi-Fi connection status when connectivity changes
            checkWifiConnection()
        }
    }
}
