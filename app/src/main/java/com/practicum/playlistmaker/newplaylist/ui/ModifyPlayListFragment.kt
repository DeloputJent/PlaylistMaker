package com.practicum.playlistmaker.newplaylist.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialib.domain.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class ModifyPlayListFragment: NewPlayListFragment() {

    val viewModel: ModifyPlayListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPlaylistId = requireArguments().getInt(PLAYLIST_KEY)

        viewModel.getPlaylistById(currentPlaylistId)
        Log.d("save", "29.onViewCreatedPlaylist="+viewModel.playListName)

        binding.apply {
            createPlaylistButton.setText(R.string.Modify_playlist_save)
            ScreenTitle.setText(R.string.Modify_playlist_modify)
        }

        viewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) {
            fillFields(it)
        }

        nameInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.PlaylistNameHint.isVisible = !s.isNullOrEmpty()
                binding.createPlaylistButton.isEnabled = !s.isNullOrEmpty()
                viewModel.playListName=s.toString()
                Log.d("save", "60.NewPlayListFragment="+s.toString())
                Log.d("save", "60.NewPlayListviewModel="+viewModel.playListName)
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        descriptionInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.PlaylistDescriptionHint.isVisible = !s.isNullOrEmpty()
                viewModel.playListDescription=s.toString()
                Log.d("save", "69.NewPlayListFragment="+s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputPlaylistName.addTextChangedListener(nameInputControl)
        binding.inputPlaylistDescription.addTextChangedListener(descriptionInputControl)

        binding.createPlaylistButton.setOnClickListener {
            var path = ""
            if (viewModel.uri!=Uri.EMPTY)
            {
               path = viewModel.saveImageToPrivateStorage(viewModel.uri, viewModel.playListName)
            }
            Log.d("save", "47.createPlaylistButton="+viewModel.playListName)
            viewModel.createPlayList(viewModel.playListName, viewModel.playListDescription,path)
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

        viewModel.uri = viewModel.getImageUri(playlist.pathToArtwork)
        viewModel.playListName = playlist.playlistName
        viewModel.playListDescription = playlist.playlistDescription

        Glide.with(this)
            .load(viewModel.uri)
            .centerCrop()
            .transform(RoundedCorners(
                dpToPx(8f, this.resources.displayMetrics).toInt()
            ))
            .placeholder(R.drawable.placeholder)
            .into(binding.PlaylistArtwork)

        binding.apply {
            inputPlaylistName.setText(viewModel.playListName)
            inputPlaylistDescription.setText(viewModel.playListDescription)
        }
    }

    companion object{
        private const val PLAYLIST_KEY = "current_playlist"

        fun createArgs(currentPlaylistId: Int): Bundle =
            bundleOf(PLAYLIST_KEY to currentPlaylistId,
            )
    }
}