package com.example.notesapplication

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NoteRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun insert(note: Note) {
        try {
            firestore.collection("notes")
                .add(note)
                .await()  // Ensure this is run on a background thread, suspending until completion
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error inserting note", e)
        }
    }

    suspend fun update(note: Note) {
        try {
            firestore.collection("notes").document(note.id)
                .set(note)
                .await()
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error updating note", e)
        }
    }

    suspend fun delete(note: Note) {
        try {
            firestore.collection("notes").document(note.id)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error deleting note", e)
        }
    }
}