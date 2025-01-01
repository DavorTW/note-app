package com.example.notesapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter(this) { note ->
            val intent = Intent(this, AddEditNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
                putExtra("note_title", note.title)
                putExtra("note_content", note.content)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
    }
}

