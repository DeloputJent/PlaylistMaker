package com.practicum.playlistmaker.ui

import android.media.MediaPlayer
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
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var pushbackbutton: ImageButton
    private lateinit var playTrackButton: ImageView

    private lateinit var playedTime: TextView

    private var mediaPlayer = Creator.getMediaPlayer()

    private var playerState = STATE_DEFAULT

    private val mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    fun startPlayer() {
        mediaPlayer.start()
        playTrackButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        timerDebounce()
    }

    fun timerCounter() {playedTime.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))}
    private val timerRunnable = Runnable {
        timerCounter()
        timerDebounce()
    }

    private fun timerDebounce() {
        mainThreadHandler?.postDelayed(timerRunnable, TIMER_CHECK_DELAY)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playTrackButton.setImageResource(R.drawable.ic_start_play_84)
        mainThreadHandler?.removeCallbacks(timerRunnable)
        playerState = STATE_PAUSED
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
        val current_Played_Track = findViewById<TextView>(R.id.current_Played_Track)
        val current_artist = findViewById<TextView>(R.id.current_artist)
        val current_track_time = findViewById<TextView>(R.id.current_track_time)
        val current_collection_name = findViewById<TextView>(R.id.current_collection_name)
        val current_track_release_year = findViewById<TextView>(R.id.current_track_release_year)
        val current_track_genre = findViewById<TextView>(R.id.current_track_genre)
        val current_track_country = findViewById<TextView>(R.id.current_track_country)
        playTrackButton = findViewById(R.id.playTrack_button)
        playedTime =findViewById<TextView>(R.id.current_played_Time)
        playedTime.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(0.0))
        val collection = findViewById<TextView>(R.id.collection)
        val track_release_year = findViewById<TextView>(R.id.track_release_year)

        val intent = intent
        val currentTrack: Track? = intent.getSerializableExtra("current_track") as? Track

        fun preparePlayer(url: String?) {
            if (url!=null) {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()

                mediaPlayer.setOnPreparedListener {
                    playerState = STATE_PREPARED
                }
                mediaPlayer.setOnCompletionListener {
                    playTrackButton.setImageResource(R.drawable.ic_start_play_84)
                    playerState = STATE_PREPARED
                    mainThreadHandler?.removeCallbacks(timerRunnable)
                    playedTime.setText("00:00")
                }
            }
        }

        pushbackbutton=findViewById(R.id.back_from_player_button)

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
        current_Played_Track.setText(currentTrack?.trackName)
        current_artist.setText(currentTrack?.artistName)
        current_track_time.setText(currentTrack?.trackTimeMillis)
        if (currentTrack?.collectionName.isNullOrEmpty()) {
            current_collection_name.visibility = View.GONE
            collection.visibility = View.GONE
        } else {current_collection_name.setText(currentTrack?.collectionName)}

        if (currentTrack?.releaseDate.isNullOrEmpty()) {
            current_track_release_year.visibility = View.GONE
            track_release_year.visibility = View.GONE
        } else {current_track_release_year.setText(currentTrack?.releaseDate)}
        current_track_genre.setText(currentTrack?.primaryGenreName)
        current_track_country.setText(currentTrack?.country)

        playTrackButton.setOnClickListener {
            when(playerState) {
                STATE_PLAYING -> {
                    pausePlayer()
                }
                STATE_PREPARED, STATE_PAUSED -> {
                    startPlayer()
                }
            }
        }

        preparePlayer(currentTrack?.previewUrl)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacks(timerRunnable)
        mediaPlayer.release()
    }
    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_CHECK_DELAY =300L
    }
}