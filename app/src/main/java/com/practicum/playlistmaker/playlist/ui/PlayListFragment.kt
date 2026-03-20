package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCurrentPlaylistBinding
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.player.ui.MusicPlayerFragment
import com.practicum.playlistmaker.playlist.ui.presentation.TracksInPlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.getValue

class PlayListFragment : Fragment() {
    private val viewModel by viewModel<PlayListViewModel>()
    private var _binding: FragmentCurrentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playListAdapter : TracksInPlaylistAdapter
    private lateinit var recyclerView : RecyclerView

    val bundle = Bundle()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentPlaylistBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentPlaylist = requireArguments().getParcelable<Playlist>(PLAYLIST_KEY)
        if (currentPlaylist==null) currentPlaylist = Playlist()

        render(currentPlaylist)

        binding.backFromPlaylistButton.setOnClickListener{
            findNavController().navigateUp()
        }

        val bottomSheetContainer = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        recyclerView = binding.tracksInPlaylist

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        playListAdapter = TracksInPlaylistAdapter(clickListener = { track ->
            bundle.putParcelable(CURRENT_TRACK, track)
            val fragment = MusicPlayerFragment()
            fragment.arguments = bundle
            findNavController().navigate(
            R.id.action_playListFragment_to_musicPlayerFragment,
            MusicPlayerFragment.createArgs(track))
        })

        recyclerView.adapter = playListAdapter

        /*viewModel.getPlaylists()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun render(playlist: Playlist) {
        val filePath = File(requireContext()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        val uri = File(filePath, playlist.pathToArtwork)

        Glide.with(this)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(
                dpToPx(8f, this.resources.displayMetrics).toInt()
            ))
            .placeholder(R.drawable.placeholder)
            .into(binding.PlaylistArtwork)
        binding.apply {
            PlaylistName.text = playlist.playlistName
            PlaylistDescription.text = playlist.playlistDescription
        }
    }

    fun showContent(playLists: List<Playlist>) {
        //playListAdapter.setPlayLists(playLists)
        recyclerView.visibility= View.VISIBLE
    }

    fun showNoPlaylistsMessage() {
        binding.apply {
            recyclerView.visibility = View.GONE
        }
    }

    companion object{
        private const val CURRENT_TRACK = "current_track"
        private const val PLAYLIST_KEY = "current_playlist"

        fun createArgs(currentPlaylist: Playlist): Bundle =
            bundleOf(PLAYLIST_KEY to currentPlaylist,
            )
    }
}