package com.example.nammamela

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val seatNo = intent.getStringExtra("seatNo") ?: "A1"
        val dramaName = intent.getStringExtra("dramaName") ?: "Nann Aase Nataka"
        val venue = intent.getStringExtra("venue") ?: "Shivamogga Town Hall"
        val address = intent.getStringExtra("address") ?: "Near Gandhi Square, Ward 5"
        val location = intent.getStringExtra("location") ?: "Shivamogga"
        val dateTime = intent.getStringExtra("dateTime") ?: "Tonight | 9:00 PM"
        val geoLocation = intent.getStringExtra("geoLocation") ?: "13.9299,75.5681"

        val tvPaymentDrama = findViewById<TextView>(R.id.tvPaymentDrama)
        val tvPaymentSeat = findViewById<TextView>(R.id.tvPaymentSeat)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etTransactionId = findViewById<EditText>(R.id.etTransactionId)
        val btnVerify = findViewById<Button>(R.id.btnPaymentDone)
        val btnPayViaUPI = findViewById<Button>(R.id.btnPayViaUPI)
        val progressBar = findViewById<ProgressBar>(R.id.paymentProgress)

        tvPaymentDrama.text = "Drama: $dramaName"
        tvPaymentSeat.text = "Selected Seat: $seatNo"

        openUPIPayment(dramaName, seatNo)
        btnPayViaUPI.setOnClickListener { openUPIPayment(dramaName, seatNo) }

        btnVerify.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            val txnId = etTransactionId.text.toString().trim()

            if (amountStr.isEmpty() || (amountStr.toIntOrNull() ?: 0) < 100) {
                Toast.makeText(this, "Valid payment of ₹100 required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (txnId.length != 12) {
                Toast.makeText(this, "Please enter the 12-digit Ref Number from your app", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnVerify.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val db = FirebaseDatabase.getInstance()
            
            // 🔥 ANTI-CHEAT: Check if this Transaction ID was already used
            db.getReference("all_payments").orderByChild("transactionRef").equalTo(txnId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Number already used by someone else!
                            progressBar.visibility = View.GONE
                            btnVerify.visibility = View.VISIBLE
                            Toast.makeText(this@PaymentActivity, "❌ Error: This Ref Number has already been used!", Toast.LENGTH_LONG).show()
                        } else {
                            // Number is unique, proceed with booking
                            processBooking(txnId, amountStr, seatNo, dramaName, venue, address, location, dateTime, geoLocation)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressBar.visibility = View.GONE
                        btnVerify.visibility = View.VISIBLE
                        Toast.makeText(this@PaymentActivity, "Database error. Try again.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun processBooking(txnId: String, amount: String, seatNo: String, drama: String, venue: String, address: String, loc: String, dt: String, geo: String) {
        val auth = FirebaseAuth.getInstance()
        val userPrefs = getSharedPreferences("users", Context.MODE_PRIVATE)
        val rawEmail = auth.currentUser?.email ?: userPrefs.getString("loggedInUser", "Guest") ?: "Guest"
        val userEmail = rawEmail.lowercase()

        val db = FirebaseDatabase.getInstance()
        val paymentRecord = mapOf(
            "userEmail" to userEmail,
            "seatNo" to seatNo,
            "amount" to amount,
            "transactionRef" to txnId,
            "drama" to drama,
            "status" to "Pending Verification",
            "timestamp" to ServerValue.TIMESTAMP
        )

        // 1. Save record for admin to check against bank statement
        db.getReference("all_payments").push().setValue(paymentRecord)
        
        // 2. Block seat globally
        db.getReference("booked_seats").child(seatNo).setValue(userEmail)

        // 3. Save to private history
        val bookingPrefs = getSharedPreferences("booking", Context.MODE_PRIVATE)
        val historyKey = "history_$userEmail"
        val oldHistory = bookingPrefs.getString(historyKey, "") ?: ""
        val newEntry = "🎟️ $drama\nSeat: $seatNo | $dt\nRef: $txnId\n-------------------\n"
        bookingPrefs.edit().putString(historyKey, newEntry + oldHistory).apply()

        // 4. Show success
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, TicketActivity::class.java)
            intent.putExtra("seatNo", seatNo)
            intent.putExtra("dramaName", drama)
            intent.putExtra("venue", venue)
            intent.putExtra("address", address)
            intent.putExtra("location", loc)
            intent.putExtra("dateTime", dt)
            intent.putExtra("geoLocation", geo)
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun openUPIPayment(dramaName: String, seatNo: String) {
        val upiUri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", "8147838763@ybl")
            .appendQueryParameter("pn", "Namma Mela")
            .appendQueryParameter("tn", "Booking: $dramaName (Seat $seatNo)")
            .appendQueryParameter("am", "100.00")
            .appendQueryParameter("cu", "INR")
            .build()
        try {
            startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_VIEW, upiUri), "Pay with"), 123)
        } catch (e: Exception) {
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            Toast.makeText(this, "Enter the 12-digit Ref Number to verify booking", Toast.LENGTH_LONG).show()
        }
    }
}
