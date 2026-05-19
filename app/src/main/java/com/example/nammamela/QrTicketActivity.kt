package com.example.nammamela

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QrTicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_qr_ticket)

        val imageQR = findViewById<ImageView>(R.id.imageQR)

        val seat = intent.getStringExtra("seat")

        val writer = QRCodeWriter()

        val bitMatrix =
            writer.encode(
                "NammaMela Ticket\nSeat: $seat",
                BarcodeFormat.QR_CODE,
                600,
                600
            )

        val width = bitMatrix.width
        val height = bitMatrix.height

        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.RGB_565
            )

        for (x in 0 until width) {
            for (y in 0 until height) {

                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y])
                        android.graphics.Color.BLACK
                    else
                        android.graphics.Color.WHITE
                )
            }
        }

        imageQR.setImageBitmap(bitmap)
    }
}