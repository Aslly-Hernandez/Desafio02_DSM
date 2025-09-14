package com.example.appcolegio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirigir directamente al Login
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}