// Note.kt (Entity)
package com.example.notesapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val pinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
