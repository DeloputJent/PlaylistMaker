package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.databinding.FragmentCurrentPlaylistBinding
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsScrollState
import com.practicum.playlistmaker.playlist.ui.presentation.TracksInPlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlayListFragment : Fragment() {

    private val viewModel by viewModel<PlayListViewModel>()
    private var _binding: FragmentCurrentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var playListAdapter : TracksInPlaylistAdapter

    private lateinit var recyclerView : RecyclerView


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentPlaylistBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backFromPlaylistButton.setOnClickListener{
            findNavController().navigateUp()
        }

        val bottomSheetContainer = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        recyclerView = binding.tracksInPlaylist

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        playListAdapter = TracksInPlaylistAdapter(/*track ->
            bundle.putParcelable(CURRENT_TRACK, track)
            val fragment = MusicPlayerFragment()
            fragment.arguments = bundle
            findNavController().navigate(
                R.id.action_searchFragment_to_musicPlayerFragment,
                MusicPlayerFragment.createArgs(track)
            )*/
        )

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

    fun render(state: PlayListsScrollState) {
        when (state) {
            is PlayListsScrollState.Content -> showContent(state.playLists)
            is PlayListsScrollState.NoPlaylistsFound -> showNoPlaylistsMessage()
            else -> showNoPlaylistsMessage()
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
        fun newInstace():PlayListFragment{
            val fragment= PlayListFragment()
            val bundle=Bundle()
            fragment.arguments=bundle
            return fragment
        }
    }
}