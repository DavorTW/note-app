package com.example.notesapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.notesapplication.R

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private val noteViewModel: NoteViewModel by viewModels()
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note2)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        // Check if we're editing an existing note
        if (intent.hasExtra("note_id")) {
            noteId = intent.getIntExtra("note_id", -1)
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
            saveNote()
        }

        buttonDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString().trim()
        val content = editTextContent.text.toString().trim()

        Log.d("AddEditNoteActivity", "Title: $title")
        Log.d("AddEditNoteActivity", "Content: $content")

        if (title.isEmpty() || content.isEmpty()) {
            // Show some error message
            Log.d("AddEditNoteActivity", "Title or content is empty")
            return
        }

        val note = Note(
            id = if (noteId != -1) noteId else 0,
            title = title,
            content = content
        )

        if (noteId == -1) {
            noteViewModel.insert(note)
            Log.d("AddEditNoteActivity", "Note inserted")
        } else {
            noteViewModel.update(note)
            Log.d("AddEditNoteActivity", "Note updated")
        }
        finish()
    }


    private fun deleteNote() {
        if (noteId != -1) {
            val note = Note(
                id = noteId,
                title = editTextTitle.text.toString(),
                content = editTextContent.text.toString()
            )
            noteViewModel.delete(note)
            Log.d("AddEditNoteActivity", "Note deleted")
        }
        finish()
    }
}
