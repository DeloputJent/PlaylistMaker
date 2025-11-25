package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.ThemeSettings

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharingInteractor = Creator.getSharingInteractor(this)

        val settingsInteractor = Creator.getSettingsInteractor(this)

        viewModel = ViewModelProvider(this, SettingsViewModel.getFactory(
            sharingInteractor, settingsInteractor))
            .get(SettingsViewModel::class.java)

        viewModel.observeThemeState()

        viewModel.settingsState.observe(this) { themeSettings ->
            viewModel.switchNightMode(themeSettings.darkThemeEnabled)
        }

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

        viewModel.observeThemeState().observe(this) {
            binding.switchDayNight.setChecked(it.darkThemeEnabled)
        }

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateSettings(ThemeSettings(checked))
        }

        binding.sharingButton.setOnClickListener{
            viewModel.shareApp()

        }

        binding.callSupportButton.setOnClickListener{
            viewModel.openSupport()

        }

        binding.userAgreementButton.setOnClickListener{
            viewModel.openTerms()
        }
    }
}