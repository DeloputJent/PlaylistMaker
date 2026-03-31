package com.practicum.playlistmaker.newplaylist.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialib.domain.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class ModifyPlayListFragment: NewPlayListFragment() {

    private val viewModel: ModifyPlayListViewModel by viewModel<ModifyPlayListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPlaylistId = requireArguments().getInt(PLAYLIST_KEY)

        viewModel.getPlaylistById(currentPlaylistId)


        binding.apply {
            createPlaylistButton.setText(R.string.Modify_playlist_save)
            ScreenTitle.setText(R.string.Modify_playlist_modify)
        }

        viewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) {
            fillFields(it)
        }

        binding.createPlaylistButton.setOnClickListener {
            var path = ""
            if (viewModel.uri!=Uri.EMPTY)
            {
                path = viewModel.saveImageToPrivateStorage(viewModel.uri,
                    viewModel.playListName)
            }
            viewModel.createPlayList(viewModel.playListName,
                viewModel.playListDescription, path)
            findNavController().navigateUp()
        }

        binding.backFromNewPlaylistButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

    }

    fun fillFields(playlist: Playlist) {

        val uri = viewModel.getImageUri(playlist.pathToArtwork)

        viewModel.playListName = playlist.playlistName
        viewModel.playListDescription = playlist.playlistDescription
        viewModel.uri = uri

        Glide.with(this)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(
                dpToPx(8f, this.resources.displayMetrics).toInt()
            ))
            .placeholder(R.drawable.placeholder)
            .into(binding.PlaylistArtwork)
        binding.apply {
            inputPlaylistName.setText(playlist.playlistName)
            inputPlaylistDescription.setText(playlist.playlistDescription)
        }
    }

    companion object{
        private const val PLAYLIST_KEY = "current_playlist"

        fun createArgs(currentPlaylistId: Int): Bundle =
            bundleOf(PLAYLIST_KEY to currentPlaylistId,
            )
    }
}