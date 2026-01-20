package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerScreenBinding
import com.practicum.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class MusicPlayerFragment: Fragment() {

    private lateinit var binding: FragmentPlayerScreenBinding
    private lateinit var viewModel: PlayerViewModel

    private fun changeButton(isPlaying: Boolean) {
        if (isPlaying) binding.playTrackButton.setImageResource(R.drawable.pause_button)
        else binding.playTrackButton.setImageResource(R.drawable.ic_start_play_84)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentTrack = requireArguments().getParcelable<Track>(TRACK_KEY)
        if (currentTrack==null) currentTrack=Track()

        viewModel=getViewModel(parameters = { parametersOf(currentTrack) })

        viewModel.observeProgressTime().observe(viewLifecycleOwner) {
            binding.currentPlayedTime.text = it
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            changeButton(it.isPlaying)
        }

        binding.backFromPlayerButton.setOnClickListener {
            findNavController().navigateUp()
        }

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

        binding.apply {
            currentPlayedTrack.text = currentTrack.trackName
            currentArtist.text = currentTrack.artistName
            currentTrackTime.text = currentTrack.trackTimeMillis
        }

        if (currentTrack.collectionName.isEmpty()) {
            binding.currentCollectionName.visibility = View.GONE
            binding.collection.visibility = View.GONE
        } else {
            binding.currentCollectionName.text = currentTrack.collectionName
        }

        if (currentTrack.releaseDate.isEmpty()) {
            binding.currentTrackReleaseYear.visibility = View.GONE
            binding.trackReleaseYear.visibility = View.GONE
        } else {
            binding.currentTrackReleaseYear.text = currentTrack.releaseDate
        }

        binding.currentTrackGenre.text = currentTrack.primaryGenreName
        binding.currentTrackCountry.text = currentTrack.country

        binding.playTrackButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object{
        private const val TRACK_KEY = "current_track"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK_KEY to track,
               )
    }
}