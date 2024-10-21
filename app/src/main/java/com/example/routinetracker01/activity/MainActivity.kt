package com.example.routinetracker01.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routinetracker01.R
import com.example.routinetracker01.adapter.RoutineAdapter
import com.example.routinetracker01.model.Routine
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : AppCompatActivity() {

    private lateinit var routineAdapter: RoutineAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnNewRoutine: Button
    private var routines = mutableListOf<Routine>()
    private var listenerRegistration: ListenerRegistration? = null
    private val db = FirebaseFirestore.getInstance()
    private var initialLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        routineAdapter = RoutineAdapter(this, routines)
        recyclerView.adapter = routineAdapter

        // Initialize "+ New" button
        btnNewRoutine = findViewById(R.id.btnNewRoutine)
        btnNewRoutine.setOnClickListener {
            val intent = Intent(this, AddRoutineActivity::class.java)
            startActivity(intent)
        }

        // Start listening for Firestore changes
        listenToRoutineChanges()
    }

    private fun listenToRoutineChanges() {
        listenerRegistration = db.collection("routines")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val newRoutines = mutableListOf<Routine>()
                snapshots?.documents?.forEach { document ->
                    val routine = document.toObject(Routine::class.java)
                    routine?.id = document.id
                    if (routine != null) {
                        newRoutines.add(routine)
                    }
                }

                // Sort routines by start time in ascending order
                newRoutines.sortBy { it.startTime }

                // Check if a new routine has been added
                if (!initialLoad && newRoutines.size > routines.size) {
                    Toast.makeText(this, "New routine added!", Toast.LENGTH_SHORT).show()
                }

                // Update the routines list and refresh the adapter
                routines.clear()
                routines.addAll(newRoutines)
                routineAdapter.updateRoutines(routines)

                // Mark initial load as complete
                initialLoad = false
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the listener when the activity is destroyed
        listenerRegistration?.remove()
    }
}
