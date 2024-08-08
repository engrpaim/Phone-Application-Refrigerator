// DataBarcode.kt
package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class Databarcode: AppCompatActivity() {

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