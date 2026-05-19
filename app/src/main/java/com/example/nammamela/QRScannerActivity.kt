package com.example.nammamela

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator

class QRScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startScanner()
    }

    private fun startScanner() {
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanner.setPrompt("Scan Ticket QR")
        scanner.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            if (result.contents == null) {
                finish()
            } else {

                val qr = result.contents   // SEAT:12

                val seatNo = qr.replace("SEAT:", "")

                val prefs = getSharedPreferences("booking", MODE_PRIVATE)

                prefs.edit()
                    .putBoolean(seatNo, true)
                    .apply()

                Toast.makeText(this, "Verified Seat $seatNo", Toast.LENGTH_SHORT).show()

                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}