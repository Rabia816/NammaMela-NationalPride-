package com.example.nammamela

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class TicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val tvTicketDramaName = findViewById<TextView>(R.id.tvTicketDramaName)
        val tvTicketVenue = findViewById<TextView>(R.id.tvTicketVenue)
        val tvTicketAddress = findViewById<TextView>(R.id.tvTicketAddress)
        val tvTicketLocation = findViewById<TextView>(R.id.tvTicketLocation)
        val tvTicketDateTime = findViewById<TextView>(R.id.tvTicketDateTime)
        val tvTicketSeat = findViewById<TextView>(R.id.tvTicketSeat)
        val imgQR = findViewById<ImageView>(R.id.imgQR)
        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnDownloadPdf = findViewById<Button>(R.id.btnDownloadPdf)
        val btnViewMap = findViewById<Button>(R.id.btnViewMap)
        val ticketCard = findViewById<View>(R.id.ticketCard)

        // GET DATA
        val dramaName = intent.getStringExtra("dramaName") ?: "Nann Aase Nataka"
        val venue = intent.getStringExtra("venue") ?: "Shivamogga Town Hall"
        val address = intent.getStringExtra("address") ?: "Near Gandhi Square, Ward 5"
        val location = intent.getStringExtra("location") ?: "Shivamogga, Karnataka"
        val dateTime = intent.getStringExtra("dateTime") ?: "Tonight | 9:00 PM"
        val seatNo = intent.getStringExtra("seatNo") ?: "A1"
        val geoLocation = intent.getStringExtra("geoLocation") ?: "13.9299,75.5681"

        // SET DATA
        tvTicketDramaName.text = dramaName
        tvTicketVenue.text = "🏛️ $venue"
        tvTicketAddress.text = "📌 $address"
        tvTicketLocation.text = "🌍 $location"
        tvTicketDateTime.text = "🕒 $dateTime"
        tvTicketSeat.text = seatNo

        // GENERATE QR
        val qrData = "TICKET_VERIFIED:$dramaName|$seatNo|$dateTime"
        val bitmap = generateQR(qrData)
        imgQR.setImageBitmap(bitmap)

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // 📄 PDF LOGIC
        btnDownloadPdf.setOnClickListener {
            saveTicketAsPdf(ticketCard, dramaName)
        }

        // 🗺️ MAP LOGIC
        btnViewMap.setOnClickListener {
            val mapUri = Uri.parse("geo:0,0?q=$geoLocation($venue)")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            
            try {
                startActivity(mapIntent)
            } catch (e: Exception) {
                // Fallback to Browser if Maps app is not available
                val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$geoLocation")
                startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }
        }
    }

    private fun saveTicketAsPdf(view: View, dramaName: String) {
        // Ensure view is drawn
        if (view.width <= 0 || view.height <= 0) {
            Toast.makeText(this, "Wait for ticket to load...", Toast.LENGTH_SHORT).show()
            return
        }

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val fileName = "NammaMela_${dramaName.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                outputStream = uri?.let { contentResolver.openOutputStream(it) }
            } else {
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                outputStream = FileOutputStream(file)
            }

            outputStream?.use {
                pdfDocument.writeTo(it)
                Toast.makeText(this, "✅ PDF Saved: Check your Downloads folder", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "❌ PDF Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    private fun generateQR(text: String): Bitmap {
        val size = 500
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bmp
    }
}
