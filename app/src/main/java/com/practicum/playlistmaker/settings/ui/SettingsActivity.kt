package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    val intentProvider = Creator.getIntentProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        binding.backtoMain.setOnClickListener {
            finish()
        }

        binding.switchDayNight.setChecked((applicationContext as App).loadTheme())

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).applyTheme(checked) }

        binding.sharingButton.setOnClickListener{
            intentProvider.shareText()
        }

        binding.callSupportButton.setOnClickListener{
            intentProvider.sendEmail()
        }

        binding.userAgreementButton.setOnClickListener{
            intentProvider.visitUrl()
        }
    }
}