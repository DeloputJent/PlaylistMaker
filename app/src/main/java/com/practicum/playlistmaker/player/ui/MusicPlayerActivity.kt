package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerScreenBinding
import com.practicum.playlistmaker.search.domain.Track

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerScreenBinding

    private lateinit var viewModel: PlayerViewModel

    private fun changeButton(isPlaying: Boolean) {
        if (isPlaying) binding.playTrackButton.setImageResource(R.drawable.pause_button)
        else binding.playTrackButton.setImageResource(R.drawable.ic_start_play_84)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        val intent = intent
        val currentTrack: Track? = intent.getSerializableExtra("current_track") as? Track

        val url:String = if (currentTrack?.previewUrl==null) "" else currentTrack.previewUrl

        viewModel = ViewModelProvider(this,PlayerViewModel.getFactory(url))
            .get(PlayerViewModel::class.java)

        viewModel.observeProgressTime().observe(this) {
            binding.currentPlayedTime.text = it
        }

        viewModel.observePlayerState().observe(this) {
            changeButton(it == PlayerViewModel.STATE_PLAYING)
        }

        binding.backFromPlayerButton.setOnClickListener {
            finish()
        }

        if (currentTrack != null) {
            Glide.with(this)
                .load(currentTrack.coverArtworkUrl)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        TypedValueCompat.dpToPx(8f, this.resources.displayMetrics).toInt()
                    )
                )
                .placeholder(R.drawable.img_placeholder_312)
                .into(binding.TrackArtwork)
        }
        binding.apply {
        currentPlayedTrack.text = currentTrack?.trackName
        currentArtist.text = currentTrack?.artistName
        currentTrackTime.text = currentTrack?.trackTimeMillis
        }
        if (currentTrack?.collectionName.isNullOrEmpty()) {
            binding.currentCollectionName.visibility = View.GONE
            binding.collection.visibility = View.GONE
        } else {
            binding.currentCollectionName.text = currentTrack.collectionName
        }

        if (currentTrack?.releaseDate.isNullOrEmpty()) {
            binding.currentTrackReleaseYear.visibility = View.GONE
            binding.trackReleaseYear.visibility = View.GONE
        } else {
            binding.currentTrackReleaseYear.text = currentTrack.releaseDate
        }
        binding.currentTrackGenre.text = currentTrack?.primaryGenreName
        binding.currentTrackCountry.text = currentTrack?.country

        binding.playTrackButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}