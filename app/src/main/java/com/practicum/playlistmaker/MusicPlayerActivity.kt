package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var pushbackbutton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player_screen)

        val currentView=findViewById<View>(R.id.MusicPlayerScreen)
        val trackArtWork = findViewById<ImageView>(R.id.TrackArtwork)
        val current_Played_Track = findViewById<TextView>(R.id.current_Played_Track)
        val current_artist = findViewById<TextView>(R.id.current_artist)
        val current_track_time = findViewById<TextView>(R.id.current_track_time)
        val current_collection_name = findViewById<TextView>(R.id.current_collection_name)
        val current_track_release_year = findViewById<TextView>(R.id.current_track_release_year)
        val current_track_genre = findViewById<TextView>(R.id.current_track_genre)
        val current_track_country = findViewById<TextView>(R.id.current_track_country)

        val collection = findViewById<TextView>(R.id.collection)
        val track_release_year = findViewById<TextView>(R.id.track_release_year)



        val intent = intent
        val currentTrack: Track? = intent.getSerializableExtra("current_track") as? Track

        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        pushbackbutton=findViewById(R.id.back_from_player_button)

        pushbackbutton.setOnClickListener {
            finish()
        }

        if (currentTrack != null) {
            Glide.with(this)
                .load(currentTrack.getCoverArtwork())
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f, this.resources.displayMetrics).toInt()))
                .placeholder(R.drawable.img_placeholder_312)
                .into(trackArtWork)
        }
        current_Played_Track.setText(currentTrack?.trackName)
        current_artist.setText(currentTrack?.artistName)
        current_track_time.setText(currentTrack?.formatTrackTime())
        if (currentTrack?.collectionName.isNullOrEmpty()) {
            current_collection_name.visibility = View.GONE
            collection.visibility = View.GONE
        } else {current_collection_name.setText(currentTrack?.collectionName)}

        if (currentTrack?.releaseDate.isNullOrEmpty()) {
            current_track_release_year.visibility = View.GONE
            track_release_year.visibility = View.GONE
        } else {current_track_release_year.setText(currentTrack?.releaseDate?.substring(0, 4))}
        current_track_genre.setText(currentTrack?.primaryGenreName)
        current_track_country.setText(currentTrack?.country)
    }
}