package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.MusicPlayerRepositoryImpl.Companion.STATE_PAUSED
import com.practicum.playlistmaker.data.MusicPlayerRepositoryImpl.Companion.STATE_PLAYING
import com.practicum.playlistmaker.data.MusicPlayerRepositoryImpl.Companion.STATE_PREPARED
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var pushbackbutton: ImageButton
    private lateinit var playTrackButton: ImageView
    private lateinit var playedTimeDisplay: TextView
    private var mediaPlayer = Creator.getMediaPlayer()
    private val mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    fun startPlayer() {
        mediaPlayer.startPlayer()
        playTrackButton.setImageResource(R.drawable.pause_button)
        timerDebounce()
    }

    fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playTrackButton.setImageResource(R.drawable.ic_start_play_84)
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    fun timerCounter(): String? = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPlayedTime())

    private val timerRunnable = Runnable {
        playedTimeDisplay.text = timerCounter()
        timerDebounce()
    }

    private fun timerDebounce() {
        mainThreadHandler?.postDelayed(timerRunnable, TIMER_CHECK_DELAY)
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
        playedTimeDisplay =findViewById<TextView>(R.id.current_played_Time)
        pushbackbutton=findViewById(R.id.back_from_player_button)

        playedTimeDisplay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0.0)

        val intent = intent
        val currentTrack: Track? = intent.getSerializableExtra("current_track") as? Track

        pushbackbutton.setOnClickListener {
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
            when(mediaPlayer.playerState) {
                STATE_PLAYING -> {
                    pausePlayer()
                }
                STATE_PREPARED, STATE_PAUSED -> {
                    startPlayer()
                }
            }
        }

        mediaPlayer.preparePlayer(currentTrack?.previewUrl, onCompletion = {
            playTrackButton.setImageResource(R.drawable.ic_start_play_84)
            mainThreadHandler?.removeCallbacks(timerRunnable)
            playedTimeDisplay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0.0)
        })
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacks(timerRunnable)
        mediaPlayer.releasePlayer()
    }
    companion object{
        private const val TIMER_CHECK_DELAY =300L
    }
}