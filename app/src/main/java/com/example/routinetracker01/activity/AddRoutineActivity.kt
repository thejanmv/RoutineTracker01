package com.example.routinetracker01.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.routinetracker01.R
import com.example.routinetracker01.model.Routine
import com.google.firebase.firestore.FirebaseFirestore

class AddRoutineActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var btnSaveRoutine: Button
    private lateinit var icBack: ImageView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_routine)

        titleEditText = findViewById(R.id.titleEditText)
        startTimeEditText = findViewById(R.id.startTimeEditText)
        btnSaveRoutine = findViewById(R.id.btnSaveRoutine)
        icBack = findViewById(R.id.icBack)

        // Set up back button functionality
        icBack.setOnClickListener {
            finish() // Close AddRoutineActivity and return to MainActivity
        }

        btnSaveRoutine.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val startTime = startTimeEditText.text.toString().trim()

            if (title.isNotEmpty() && startTime.isNotEmpty()) {
                addNewRoutine(title, startTime)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewRoutine(title: String, startTime: String) {
        val newRoutine = Routine(title = title, startTime = startTime)

        // Add the new routine to Firestore
        db.collection("routines").add(newRoutine)
            .addOnSuccessListener {
                Toast.makeText(this, "Routine added successfully", Toast.LENGTH_SHORT).show()

                // Return to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add routine: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
