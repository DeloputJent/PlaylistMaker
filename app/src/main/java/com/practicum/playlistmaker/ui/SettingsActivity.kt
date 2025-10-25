package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator.getIntentProvider
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    val intentProvider = getIntentProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val currentView = findViewById<View>(R.id.settings)
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        val pushbackbutton = findViewById<Button>(R.id.backtoMain)
        val pushshare = findViewById<Button>(R.id.sharingButton)
        val pushsupport = findViewById<Button>(R.id.callSupportButton)
        val pushagreement = findViewById<Button>(R.id.userAgreementButton)

        pushbackbutton.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switchDayNight)

        themeSwitcher.setChecked((applicationContext as App).loadTheme())

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).applyTheme(checked) }

        pushshare.setOnClickListener{
            intentProvider.shareText()
        }

        pushsupport.setOnClickListener{
            intentProvider.sendEmail()
        }

        pushagreement.setOnClickListener{
            intentProvider.visitUrl()
        }
    }
}