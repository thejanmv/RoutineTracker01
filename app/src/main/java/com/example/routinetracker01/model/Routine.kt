package com.example.routinetracker01.model

data class Routine(
    var id: String = "", // ID for Firestore document
    val title: String = "",
    val startTime: String = ""
)
