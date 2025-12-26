package com.practicum.playlistmaker.root

import android.os.Bundle
import androidx.activity.enableEdgeToEdge


import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R

import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.search.ui.SearchFragment


class RootActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, SearchFragment())
            }
        }
    }
}