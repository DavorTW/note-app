package com.example.notesapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    private var firestoreListener: ListenerRegistration? = null

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        firestoreListener = firestore.collection("notes")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("NoteViewModel", "Error listening to Firestore changes", error)
                    return@addSnapshotListener
                }
                snapshots?.let {
                    val notes = it.documents.mapNotNull { doc ->
                        doc.toObject(Note::class.java)?.apply {
                            id = doc.id
                        }
                    }
                    _notes.postValue(notes)
                }
            }
    }

    fun insert(note: Note) {
        firestore.collection("notes")
            .add(note)
            .addOnSuccessListener {
                // Optionally notify user or perform other tasks after successful insert
            }
    }

    fun update(note: Note) {
        firestore.collection("notes").document(note.id)
            .set(note)
            .addOnSuccessListener {
                // Optionally notify user or perform other tasks after successful update
            }
    }

    fun delete(note: Note) {
        firestore.collection("notes").document(note.id)
            .delete()
            .addOnSuccessListener {
                // Optionally notify user or perform other tasks after successful delete
            }
    }

    override fun onCleared() {
        super.onCleared()
        firestoreListener?.remove()
    }
}