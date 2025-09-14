package com.example.appcolegio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class NotesListActivity : AppCompatActivity() {

    private lateinit var recyclerNotes: RecyclerView
    private lateinit var notesList: ArrayList<Note>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        recyclerNotes = findViewById(R.id.recyclerNotes)
        recyclerNotes.layoutManager = LinearLayoutManager(this)
        notesList = ArrayList()

        dbRef = FirebaseDatabase.getInstance().getReference("notes")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notesList.clear()
                if (snapshot.exists()) {
                    for (noteSnap in snapshot.children) {
                        val note = noteSnap.getValue(Note::class.java)
                        if (note != null) {
                            notesList.add(note)
                        }
                    }
                    recyclerNotes.adapter = NoteAdapter(notesList) { note ->
                        // Click en nota: abrir EditNoteActivity
                        val intent = Intent(this@NotesListActivity, EditNoteActivity::class.java)
                        intent.putExtra("noteId", note.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NotesListActivity, "Error al cargar notas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
