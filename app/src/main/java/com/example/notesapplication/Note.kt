// Note.kt (Firestore Model)
package com.example.notesapplication

data class Note(
    var id: String = "",
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
