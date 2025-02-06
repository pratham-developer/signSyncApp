package com.example.signsyncapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 2 seconds, then start the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Starting_page::class.java))
            finish() // Close splash activity
        }, 2000) // 2000 ms = 2 seconds
    }
}
