package com.example.notesapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private val noteViewModel: NoteViewModel by viewModels()
    private var noteId: String? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note2)

        firestore = FirebaseFirestore.getInstance()

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        // Check if we're editing an existing note
        if (intent.hasExtra("note_id")) {
            noteId = intent.getStringExtra("note_id")
            val noteTitle = intent.getStringExtra("note_title") ?: ""
            val noteContent = intent.getStringExtra("note_content") ?: ""

            editTextTitle.setText(noteTitle)
            editTextContent.setText(noteContent)
            buttonDelete.isEnabled = true
        } else {
            buttonDelete.isEnabled = false
        }

        buttonSave.setOnClickListener {
            Log.d("AddEditNoteActivity", "Save button clicked")
            it.isEnabled = false
            saveNote()
        }

        buttonDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString().trim()
        val content = editTextContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Log.d("AddEditNoteActivity", "Title or content is empty")
            return
        }

        val note = hashMapOf(
            "title" to title,
            "content" to content
        )

        if (noteId == null) {
            firestore.collection("notes")
                .add(note)
                .addOnSuccessListener {
                    Log.d("AddEditNoteActivity", "Note added with ID: ${it.id}")
                    finish()
                }
                .addOnFailureListener {
                    Log.e("AddEditNoteActivity", "Error adding note", it)
                }
        } else {
            firestore.collection("notes").document(noteId!!)
                .set(note)
                .addOnSuccessListener {
                    Log.d("AddEditNoteActivity", "Note updated")
                    finish()
                }
                .addOnFailureListener {
                    Log.e("AddEditNoteActivity", "Error updating note", it)
                }
        }
    }

    private fun deleteNote() {
        if (noteId != null) {
            firestore.collection("notes").document(noteId!!)
                .delete()
                .addOnSuccessListener {
                    Log.d("AddEditNoteActivity", "Note deleted")
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("AddEditNoteActivity", "Error deleting note", e)
                }
        } else {
            Log.e("AddEditNoteActivity", "Note ID is null")
        }
    }
}
