package com.example.appcolegio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class StudentsListActivity : AppCompatActivity() {

    private lateinit var recyclerStudents: RecyclerView
    private lateinit var studentList: ArrayList<Student>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        recyclerStudents = findViewById(R.id.recyclerStudents)
        recyclerStudents.layoutManager = LinearLayoutManager(this)
        studentList = ArrayList()

        dbRef = FirebaseDatabase.getInstance().getReference("estudiantes")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                if (snapshot.exists()) {
                    for (sSnap in snapshot.children) {
                        val student = sSnap.getValue(Student::class.java)
                        if (student != null) studentList.add(student)
                    }
                    recyclerStudents.adapter = StudentAdapter(studentList) { student ->
                        val intent = Intent(this@StudentsListActivity, EditStudentActivity::class.java)
                        intent.putExtra("studentId", student.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}