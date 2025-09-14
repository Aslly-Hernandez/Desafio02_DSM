package com.example.appcolegio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddStudentActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        etDireccion = findViewById(R.id.etDireccion)
        etTelefono = findViewById(R.id.etTelefono)
        btnGuardar = findViewById(R.id.btnGuardar)

        dbRef = FirebaseDatabase.getInstance().getReference("estudiantes")

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val edadStr = etEdad.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()

            if (nombre.isEmpty() || edadStr.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val edad = edadStr.toIntOrNull()
            if (edad == null || edad <= 0) {
                Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = dbRef.push().key ?: ""
            val student = Student(id, nombre, edad, direccion, telefono)
            dbRef.child(id).setValue(student).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Estudiante guardado", Toast.LENGTH_SHORT).show()
                    etNombre.text.clear()
                    etEdad.text.clear()
                    etDireccion.text.clear()
                    etTelefono.text.clear()
                } else {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}