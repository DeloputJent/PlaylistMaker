package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.MediaLibActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentView=findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }


        binding.setbutton.setOnClickListener {
            val displaySettingsIntent= Intent(this, SettingsActivity::class.java)
            startActivity(displaySettingsIntent)
        }

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayLibIntent= Intent(this@MainActivity, MediaLibActivity::class.java)
                startActivity(displayLibIntent)
            }
        }

        binding.libbutton.setOnClickListener(buttonClickListener)

        val button2ClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displaySearchIntent= Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearchIntent)
            }
        }
        binding.searchbutton.setOnClickListener(button2ClickListener)
    }
}