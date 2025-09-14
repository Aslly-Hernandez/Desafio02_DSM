package com.example.appcolegio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    private lateinit var btnAddStudent: Button
    private lateinit var btnAddNote: Button
    private lateinit var btnListStudents: Button
    private lateinit var btnListNotes: Button
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        auth = FirebaseAuth.getInstance()

        btnAddStudent = findViewById(R.id.btnAddStudent)
        btnAddNote = findViewById(R.id.btnAddNote)
        btnListStudents = findViewById(R.id.btnListStudents)
        btnListNotes = findViewById(R.id.btnListNotes)
        btnLogout = findViewById(R.id.btnLogout)

        btnAddStudent.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }

        btnAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        btnListStudents.setOnClickListener {
            startActivity(Intent(this, StudentsListActivity::class.java))
        }

        btnListNotes.setOnClickListener {
            startActivity(Intent(this, ListNotesActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}