package com.practicum.playlistmaker
import android.content.Intent
import android.view.View
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val currentView=findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }


        val pushsearch = findViewById<Button>(R.id.searchbutton)
        val pushmedialib = findViewById<Button>(R.id.libbutton)
        val pushsettings = findViewById<Button>(R.id.setbutton)

        pushsettings.setOnClickListener {
            val displaySettingsIntent= Intent(this, SettingsActivity::class.java)
            startActivity(displaySettingsIntent)
        }

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayLibIntent= Intent(this@MainActivity, MediaLibActivity::class.java)
                startActivity(displayLibIntent)
            }
        }

        pushmedialib.setOnClickListener(buttonClickListener)

        val button2ClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displaySearchIntent= Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearchIntent)
            }
        }
        pushsearch.setOnClickListener(button2ClickListener)
    }

}