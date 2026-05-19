package com.example.nammamela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class FanWallActivity : AppCompatActivity() {

    private lateinit var etComment: EditText
    private lateinit var btnPost: Button
    private lateinit var tvComments: TextView
    
    // Using Firebase so comments show on everyone's phone
    private val database = FirebaseDatabase.getInstance().getReference("fan_comments")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fan_wall)

        etComment = findViewById(R.id.etComment)
        btnPost = findViewById(R.id.btnPost)
        tvComments = findViewById(R.id.tvComments)

        // Listen for new comments from anyone in real-time
        listenForComments()

        btnPost.setOnClickListener {
            val commentText = etComment.text.toString().trim()

            if (commentText.isEmpty()) {
                Toast.makeText(this, "Please write a comment", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            postComment(commentText)
            etComment.text.clear()
        }
    }

    private fun postComment(text: String) {
        val postId = database.push().key ?: return
        val post = Post(text = text, time = System.currentTimeMillis())
        
        database.child(postId).setValue(post).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Comment posted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listenForComments() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sb = StringBuilder()
                // We reverse the order to show NEWEST comments at the TOP
                val children = snapshot.children.reversed()
                
                for (postSnapshot in children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        sb.append("💬 ").append(post.text).append("\n\n")
                    }
                }

                tvComments.text = if (sb.isEmpty()) {
                    "Be the first to comment! 💬"
                } else {
                    sb.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FanWallActivity, "Database Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
