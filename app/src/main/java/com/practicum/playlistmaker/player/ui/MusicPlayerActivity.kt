package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
import com.practicum.playlistmaker.search.domain.Track

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var pushBackButton: ImageButton
    private lateinit var playTrackButton: ImageView
    private lateinit var playedTimeDisplay: TextView
    private lateinit var viewModel: PlayerViewModel

    private fun changeButton(isPlaying: Boolean) {
        if (isPlaying) playTrackButton.setImageResource(R.drawable.pause_button)
        else playTrackButton.setImageResource(R.drawable.ic_start_play_84)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player_screen)

        val currentView=findViewById<View>(R.id.MusicPlayerScreen)

        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        val trackArtWork = findViewById<ImageView>(R.id.TrackArtwork)
        val currentPlayedTrack = findViewById<TextView>(R.id.current_Played_Track)
        val currentArtist = findViewById<TextView>(R.id.current_artist)
        val currentTrackTime = findViewById<TextView>(R.id.current_track_time)
        val currentCollectionName = findViewById<TextView>(R.id.current_collection_name)
        val currentTrackReleaseYear = findViewById<TextView>(R.id.current_track_release_year)
        val currentTrackGenre = findViewById<TextView>(R.id.current_track_genre)
        val currentTrackCountry = findViewById<TextView>(R.id.current_track_country)
        val collection = findViewById<TextView>(R.id.collection)
        val trackReleaseYear = findViewById<TextView>(R.id.track_release_year)

        playTrackButton = findViewById(R.id.playTrack_button)
        playedTimeDisplay =findViewById(R.id.current_played_Time)
        pushBackButton=findViewById(R.id.back_from_player_button)

        val intent = intent
        val currentTrack: Track? = intent.getSerializableExtra("current_track") as? Track

        val url:String = if (currentTrack?.previewUrl==null) "" else currentTrack.previewUrl

        viewModel = ViewModelProvider(this,PlayerViewModel.getFactory(url))
            .get(PlayerViewModel::class.java)

        //playedTimeDisplay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0.0)

        viewModel.observeProgressTime().observe(this) {
            playedTimeDisplay.text = it
        }

        viewModel.observePlayerState().observe(this) {
            changeButton(it != PlayerViewModel.STATE_PLAYING)
        }

        pushBackButton.setOnClickListener {
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
                .into(trackArtWork)
        }
        currentPlayedTrack.text = currentTrack?.trackName
        currentArtist.text = currentTrack?.artistName
        currentTrackTime.text = currentTrack?.trackTimeMillis
        if (currentTrack?.collectionName.isNullOrEmpty()) {
            currentCollectionName.visibility = View.GONE
            collection.visibility = View.GONE
        } else {
            currentCollectionName.text = currentTrack.collectionName
        }

        if (currentTrack?.releaseDate.isNullOrEmpty()) {
            currentTrackReleaseYear.visibility = View.GONE
            trackReleaseYear.visibility = View.GONE
        } else {
            currentTrackReleaseYear.text = currentTrack.releaseDate
        }
        currentTrackGenre.text = currentTrack?.primaryGenreName
        currentTrackCountry.text = currentTrack?.country

        playTrackButton.setOnClickListener {
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