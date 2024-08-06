package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.BarcodeResult
import com.google.zxing.ResultPoint

class ScannerActivity : AppCompatActivity() {

    private lateinit var barcodeView: BarcodeView
    private lateinit var scanResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        barcodeView = findViewById(R.id.barcode_view)
        scanResultTextView = findViewById(R.id.scan_result)

        // Set the BarcodeView size
        setBarcodeViewSize(1.0, 1.0) // 3x3 inches

        // Configure BarcodeView
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    // Handle the barcode result
                    scanResultTextView.text = "Scan result: ${it.text}"
                    // Optionally, you can stop scanning after a successful result
                    barcodeView.pause()
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>?) {
                // Optional: Handle possible result points
                // You can use this to highlight detected points in your UI
                resultPoints?.forEach { point ->
                    // Handle each point (e.g., draw markers on a view)
                }
            }
        })
    }

    private fun setBarcodeViewSize(widthInInches: Double, heightInInches: Double) {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val densityDpi = metrics.densityDpi

        val widthPixels = (widthInInches * densityDpi).toInt()
        val heightPixels = (heightInInches * densityDpi).toInt()

        val layoutParams = barcodeView.layoutParams
        layoutParams.width = widthPixels
        layoutParams.height = heightPixels
        barcodeView.layoutParams = layoutParams
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()  // Start scanning
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()  // Pause scanning
    }
}
