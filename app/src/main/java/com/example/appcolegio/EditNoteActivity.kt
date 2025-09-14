package com.example.appcolegio

import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditNoteActivity : AppCompatActivity() {

    private lateinit var spinnerStudent: Spinner
    private lateinit var spinnerGrade: Spinner
    private lateinit var spinnerSubject: Spinner
    private lateinit var etFinalNote: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnCancel: Button

    private lateinit var notesRef: DatabaseReference
    private lateinit var studentsRef: DatabaseReference

    private var noteId: String = ""
    private var studentsNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        spinnerStudent = findViewById(R.id.spinnerEditStudent)
        spinnerGrade = findViewById(R.id.spinnerEditGrade)
        spinnerSubject = findViewById(R.id.spinnerEditSubject)
        etFinalNote = findViewById(R.id.etEditFinalNote)
        btnUpdate = findViewById(R.id.btnUpdateNote)
        btnDelete = findViewById(R.id.btnDeleteNote)
        btnCancel = findViewById(R.id.btnCancelNote)

        notesRef = FirebaseDatabase.getInstance().getReference("notes")
        studentsRef = FirebaseDatabase.getInstance().getReference("estudiantes")

        noteId = intent.getStringExtra("noteId") ?: ""
        if (noteId.isBlank()) {
            Toast.makeText(this, "ID de nota inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val grades = listOf("1°","2°","3°","4°","5°")
        val subjects = listOf("Matemáticas","Lenguaje","Ciencias","Historia")

        spinnerGrade.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        spinnerSubject.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)

        loadStudentsThenNote()

        btnUpdate.setOnClickListener { updateNote() }
        btnDelete.setOnClickListener { confirmDelete() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun loadStudentsThenNote() {
        studentsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentsNames.clear()
                for (child in snapshot.children) {
                    val student = child.getValue(Student::class.java)
                    if (student != null) studentsNames.add(student.nombre)
                }
                if (studentsNames.isEmpty()) studentsNames.add("Sin estudiantes")
                spinnerStudent.adapter = ArrayAdapter(this@EditNoteActivity, android.R.layout.simple_spinner_item, studentsNames)
                loadNote()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditNoteActivity, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadNote() {
        notesRef.child(noteId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val note = snapshot.getValue(Note::class.java)
                if (note != null) {
                    spinnerStudent.setSelection(studentsNames.indexOf(note.student))
                    (spinnerGrade.adapter as ArrayAdapter<String>).getPosition(note.grade).let { if(it>=0) spinnerGrade.setSelection(it) }
                    (spinnerSubject.adapter as ArrayAdapter<String>).getPosition(note.subject).let { if(it>=0) spinnerSubject.setSelection(it) }
                    etFinalNote.setText(note.finalNote.toString())
                } else {
                    Toast.makeText(this@EditNoteActivity, "Nota no encontrada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditNoteActivity, "Error al cargar nota", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateNote() {
        val student = spinnerStudent.selectedItem.toString()
        val grade = spinnerGrade.selectedItem.toString()
        val subject = spinnerSubject.selectedItem.toString()
        val noteText = etFinalNote.text.toString().trim()

        val finalNote = noteText.toDoubleOrNull()
        if (finalNote == null || finalNote !in 0.0..10.0) {
            Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        val updated = Note(id = noteId, student = student, grade = grade, subject = subject, finalNote = finalNote)
        notesRef.child(noteId).setValue(updated)
            .addOnSuccessListener { Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show(); finish() }
            .addOnFailureListener { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Eliminar esta nota?")
            .setPositiveButton("Sí") { _: DialogInterface, _: Int -> deleteNote() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteNote() {
        notesRef.child(noteId).removeValue()
            .addOnSuccessListener { Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show(); finish() }
            .addOnFailureListener { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
    }
}