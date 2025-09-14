package com.example.appcolegio

import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditStudentActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnCancel: Button

    private lateinit var dbRef: DatabaseReference
    private var studentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        etNombre = findViewById(R.id.etEditNombre)
        etEdad = findViewById(R.id.etEditEdad)
        etDireccion = findViewById(R.id.etEditDireccion)
        etTelefono = findViewById(R.id.etEditTelefono)
        btnUpdate = findViewById(R.id.btnUpdateStudent)
        btnDelete = findViewById(R.id.btnDeleteStudent)
        btnCancel = findViewById(R.id.btnCancelStudent)

        dbRef = FirebaseDatabase.getInstance().getReference("estudiantes")

        studentId = intent.getStringExtra("studentId") ?: ""
        if (studentId.isBlank()) {
            Toast.makeText(this, "ID de estudiante inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadStudent()

        btnUpdate.setOnClickListener { updateStudent() }
        btnDelete.setOnClickListener { confirmDelete() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun loadStudent() {
        dbRef.child(studentId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val student = snapshot.getValue(Student::class.java)
                if (student != null) {
                    etNombre.setText(student.nombre)
                    etEdad.setText(student.edad.toString())
                    etDireccion.setText(student.direccion)
                    etTelefono.setText(student.telefono)
                } else {
                    Toast.makeText(this@EditStudentActivity, "Estudiante no encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditStudentActivity, "Error al cargar: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateStudent() {
        val nombre = etNombre.text.toString().trim()
        val edad = etEdad.text.toString().toIntOrNull()
        val direccion = etDireccion.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (nombre.isEmpty() || edad == null || edad <= 0 || direccion.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val updated = Student(studentId, nombre, edad, direccion, telefono)
        dbRef.child(studentId).setValue(updated)
            .addOnSuccessListener { Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show(); finish() }
            .addOnFailureListener { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Eliminar este estudiante?")
            .setPositiveButton("Sí") { _: DialogInterface, _: Int -> deleteStudent() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteStudent() {
        dbRef.child(studentId).removeValue()
            .addOnSuccessListener { Toast.makeText(this, "Estudiante eliminado", Toast.LENGTH_SHORT).show(); finish() }
            .addOnFailureListener { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
    }
}