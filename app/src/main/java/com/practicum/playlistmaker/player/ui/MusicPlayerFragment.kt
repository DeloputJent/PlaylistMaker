package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerScreenBinding
import com.practicum.playlistmaker.player.ui.presentation.PlayListOnMPAdapter
import com.practicum.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class MusicPlayerFragment: Fragment() {

    private lateinit var binding: FragmentPlayerScreenBinding
    private lateinit var viewModel: PlayerViewModel

    private lateinit var playListAdapter : PlayListOnMPAdapter

    private lateinit var recyclerView : RecyclerView

    private fun changeButton(isPlaying: Boolean) {
        if (isPlaying) binding.playTrackButton.setImageResource(R.drawable.pause_button)
        else binding.playTrackButton.setImageResource(R.drawable.ic_start_play_84)
    }

    private fun setFavoriteButton(isTrackFavorite: Boolean) {
        if (isTrackFavorite) binding.ToFavoriteButton.setImageResource(R.drawable.ic_to_favorite_pressed_51)
        else binding.ToFavoriteButton.setImageResource(R.drawable.ic_to_favorite_51)
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

        val bottomSheetContainer = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        recyclerView = binding.PlaylistsOnPlayer

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        playListAdapter = PlayListOnMPAdapter(
            clickListener = { playlist ->
                run {
                    if (viewModel.addTrackToPlayList(playlist)) {
                    val toast = Toast(requireContext())
                    toast.duration = Toast.LENGTH_SHORT
                    toast.setText(
                        getString(
                            R.string.new_track_added_to_playlist,
                            playlist.playlistName
                        )
                    )
                    toast.show()}
                }
            }
        )

        viewModel.getPlaylists()

        viewModel.observePlaylistLiveData().observe(viewLifecycleOwner) {
            playListAdapter.setPlayLists(it)
            recyclerView.visibility= View.VISIBLE
        }

        recyclerView.adapter = playListAdapter

        binding.buttonMakePlayList.setOnClickListener {
            findNavController().navigate(R.id.action_musicPlayerFragment_to_newPlayListFragment)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            changeButton(it.isPlaying)
            binding.currentPlayedTime.text = it.progress
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner){
            setFavoriteButton(it)
        }

        binding.backFromPlayerButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playTrackButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.ToFavoriteButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.ToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

        val overlay = binding.overlay

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

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