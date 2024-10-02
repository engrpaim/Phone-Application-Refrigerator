package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.openScanner)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val openScannerButton: Button = findViewById(R.id.openScanner)
        openScannerButton.setOnClickListener {
            val scannerOpener = Intent(this, ScannerActivity::class.java)
            startActivity(scannerOpener)
        }

        // Get and display IP, MAC address, and device name
        val ipAddressTextView: TextView = findViewById(R.id.ipAddress)
        val macAddressTextView: TextView = findViewById(R.id.macAddress)
        val deviceNameTextView: TextView = findViewById(R.id.deviceName) // Add this TextView in your layout

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = intToIp(wifiManager.connectionInfo.ipAddress)
        val macAddress = wifiManager.connectionInfo.macAddress ?: "Unavailable" // Handle potential null

        // Get the device name
        val deviceName = Build.MODEL




        ipAddressTextView.text = "IP Address: $ipAddress"
        macAddressTextView.text = "MAC Address: $macAddress"
        
    }

    private fun intToIp(ip: Int): String {
        return (ip shr 0 and 0xff).toString() + "." +
                (ip shr 8 and 0xff).toString() + "." +
                (ip shr 16 and 0xff).toString() + "." +
                (ip shr 24 and 0xff).toString()
    }
}
