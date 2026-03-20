package com.practicum.playlistmaker.root

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge


import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope


import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R

import com.practicum.playlistmaker.databinding.ActivityRootBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RootActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedTab", bottomNavigationView.selectedItemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.selectedItemId=R.id.mediaLibFragment

        if (savedInstanceState != null) {
            val selectedTab = savedInstanceState.getInt("selectedTab")
            bottomNavigationView.selectedItemId = selectedTab
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.musicPlayerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.newPlayListFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}
