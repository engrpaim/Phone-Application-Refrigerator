package com.example.myapplication



import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class ScannerActivity : AppCompatActivity() {

    private lateinit var scanResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        scanResultTextView = findViewById(R.id.scan_result)

        // Start QR scan
        startScan()
    }

    private fun startScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Place the QR code inside the frame")
        integrator.setCameraId(0) // Use the rear camera
        integrator.setBeepEnabled(true) // Optional beep on scan
        integrator.setBarcodeImageEnabled(true) // Optional save scan image
        integrator.setOrientationLocked(false) // Allow orientation changes
        //integrator.setCaptureActivity(CustomCaptureActivity::class.java) // Use custom activity
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Handle scan cancelation
                scanResultTextView.text = "Scan canceled"
            } else {
                // Display scan result
                val scannedResult = result.contents
                scanResultTextView.text = "Scan result: $scannedResult"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
