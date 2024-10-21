package com.example.routinetracker01.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.routinetracker01.R
import com.example.routinetracker01.model.Routine
import com.google.firebase.firestore.FirebaseFirestore

class RoutineAdapter(
    private val context: Context,
    private var routines: MutableList<Routine>
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]
        holder.tvRoutineTitle.text = routine.title
        holder.tvRoutineTimes.text = routine.startTime

        // Set up delete icon click listener
        holder.iconDelete.setOnClickListener {
            deleteRoutine(routine, position)
        }
    }

    override fun getItemCount(): Int = routines.size

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoutineTitle: TextView = itemView.findViewById(R.id.tvRoutineTitle)
        val tvRoutineTimes: TextView = itemView.findViewById(R.id.tvRoutineTimes)
        val iconDelete: ImageView = itemView.findViewById(R.id.iconDelete)
    }

    // Function to update routines list and refresh the adapter
    fun updateRoutines(newRoutines: MutableList<Routine>) {
        routines = newRoutines
        notifyDataSetChanged()
    }

    // Function to delete routine from Firestore and update the list
    private fun deleteRoutine(routine: Routine, position: Int) {
        if (routine.id.isNotEmpty()) {
            db.collection("routines").document(routine.id).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Routine deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to delete routine: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Routine ID is invalid", Toast.LENGTH_SHORT).show()
        }
    }
}
