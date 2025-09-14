package com.example.appcolegio

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNoteActivity : AppCompatActivity() {

    private lateinit var spinnerStudent: Spinner
    private lateinit var spinnerGrade: Spinner
    private lateinit var spinnerSubject: Spinner
    private lateinit var etFinalNote: EditText
    private lateinit var btnSaveNote: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        spinnerStudent = findViewById(R.id.spinnerStudent)
        spinnerGrade = findViewById(R.id.spinnerGrade)
        spinnerSubject = findViewById(R.id.spinnerSubject)
        etFinalNote = findViewById(R.id.etFinalNote)
        btnSaveNote = findViewById(R.id.btnSaveNote)

        dbRef = FirebaseDatabase.getInstance().getReference("notes")

        val students = listOf("Juan Pérez", "Ana López")
        val grades = listOf("1°", "2°", "3°", "4°", "5°")
        val subjects = listOf("Matemáticas", "Lenguaje", "Ciencias", "Historia")

        spinnerStudent.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, students)
        spinnerGrade.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        spinnerSubject.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)

        btnSaveNote.setOnClickListener {
            val student = spinnerStudent.selectedItem.toString()
            val grade = spinnerGrade.selectedItem.toString()
            val subject = spinnerSubject.selectedItem.toString()
            val noteText = etFinalNote.text.toString()

            if (noteText.isBlank()) {
                Toast.makeText(this, "Ingrese la nota", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalNote = noteText.toDoubleOrNull()
            if (finalNote == null || finalNote < 0 || finalNote > 10) {
                Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val noteId = dbRef.push().key ?: ""
            val note = Note(noteId, student, grade, subject, finalNote)

            dbRef.child(noteId).setValue(note).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
                    etFinalNote.text.clear()
                } else {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}