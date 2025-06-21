package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        enableEdgeToEdge()

        val pushbackbutton=findViewById<Button>(R.id.backtoMain)

        pushbackbutton.setOnClickListener {
            val backToMainIntent= Intent(this, MainActivity::class.java)
            startActivity(backToMainIntent)
        }

    }
}