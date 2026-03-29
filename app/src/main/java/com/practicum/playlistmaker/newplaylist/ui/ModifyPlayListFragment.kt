package com.practicum.playlistmaker.newplaylist.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import java.io.File
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
             var path =""
             if (uri!=Uri.EMPTY)
            {
                path = viewModel.saveImageToPrivateStorage(uri, playListName)
            }
            viewModel.createPlayList(playListName, playListDescription, path)
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
        val filePath = File(requireContext()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        val file = File(filePath, playlist.pathToArtwork)
        //uri = Uri.fromFile(file)
        Glide.with(this)
            .load(file)
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