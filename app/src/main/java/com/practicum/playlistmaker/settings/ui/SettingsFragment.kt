package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        viewModel.observeThemeState()

        viewModel.settingsState.observe(viewLifecycleOwner) { themeSettings ->
            viewModel.switchNightMode(themeSettings.darkThemeEnabled)
        }

        viewModel.observeThemeState().observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}