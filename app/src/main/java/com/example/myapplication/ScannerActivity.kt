package com.example.myapplication


import android.os.Bundle
import android.util.DisplayMetrics
import android.content.Intent
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
        setBarcodeViewSize(1.2, 1.2)

        // Configure BarcodeView
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {

                    val barcodeData = it.text;

                    splitBarcodeData(barcodeData)
                    barcodeView.pause()
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>?) {

                resultPoints?.forEach { point ->

                }
            }
        })
    }

    private fun splitBarcodeData(barcodeData: String) {
        // Split the barcode data by semicolon
        val splitData = barcodeData.split(";")

        // Process each piece of data
        val processedData = splitData.map { data ->
            // Check if the data contains an underscore
            if (data.contains('_')) {
                // Remove all characters after and including the underscore
                data.substringBefore('_')
            } else {
                // Return the data as is if it doesn't contain an underscore
                data
            }

        }

        // Create a string with each piece of processed data on a new line
        val displayText = processedData.joinToString(separator = ";")
        val intent = Intent(this, Databarcode::class.java)
        // Add the displayText as an extra in the intent

        // Start the new activity

        intent.putExtra("DISPLAY_TEXT", displayText)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
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
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}
