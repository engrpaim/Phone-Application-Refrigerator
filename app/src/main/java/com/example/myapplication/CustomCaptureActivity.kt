package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.BarcodeResult
import com.google.zxing.ResultPoint

class CustomCaptureActivity : CaptureActivity() {

    private lateinit var barcodeView: BarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_capture)

        val barcodeViewWidth = resources.getDimensionPixelSize(R.dimen.barcode_view_width)
        val barcodeViewHeight = resources.getDimensionPixelSize(R.dimen.barcode_view_height)

        // Find the BarcodeView and adjust its size programmatically
        val barcodeView = findViewById<BarcodeView>(R.id.barcode_view)
        val layoutParams = barcodeView.layoutParams
        layoutParams.width = barcodeViewWidth
        layoutParams.height = barcodeViewHeight
        barcodeView.setLayoutParams(layoutParams)



        // Configure BarcodeView
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    // Handle the barcode result
                    val resultIntent = Intent().apply {
                        putExtra("SCAN_RESULT", it.text)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
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

    override fun onResume() {
        super.onResume()
        barcodeView.resume()  // Start scanning
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()  // Pause scanning
    }
}
