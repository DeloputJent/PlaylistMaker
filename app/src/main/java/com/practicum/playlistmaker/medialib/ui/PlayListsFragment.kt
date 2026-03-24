package com.practicum.playlistmaker.medialib.ui

import android.graphics.Rect
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsAdapter
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsScrollState
import com.practicum.playlistmaker.player.ui.MusicPlayerFragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlayListsFragment : Fragment() {
    private val viewModel by viewModel< PlayListsViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var playListAdapter : PlayListsAdapter
    private lateinit var recyclerView : RecyclerView
    val bundle = Bundle()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonAddNewPlayList.setOnClickListener{
            findNavController().navigate(R.id.action_mediaLibFragment_to_newPlayListFragment)
        }

        recyclerView = binding.PlayListsScroll

        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        playListAdapter = PlayListsAdapter(
            clickListener = { playlist ->
                run {
                    bundle.putInt(PLAYLIST_KEY, playlist.playlistID)
                    val fragment = MusicPlayerFragment()
                    fragment.arguments = bundle
                    findNavController().navigate(
                        R.id.action_mediaLibFragment_to_playListFragment,
                        bundle
                    )
                }
            }
        )

        recyclerView.adapter= playListAdapter

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val dp8 = resources.getDimensionPixelSize(R.dimen.spacing_8dp)
                val dp16 = resources.getDimensionPixelSize(R.dimen.spacing_16dp)
                outRect.left = dp8
                outRect.right = dp8
                outRect.bottom = dp16
            }
        })

        viewModel.getPlaylists()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
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
        binding.noPlayListMessage.visibility = View.GONE
        playListAdapter.setPlayLists(playLists)
        recyclerView.visibility= View.VISIBLE
    }

    fun showNoPlaylistsMessage() {
        binding.apply {
            recyclerView.visibility = View.GONE
            noPlayListMessage.visibility = View.VISIBLE
        }
    }

    companion object{
        private const val PLAYLIST_KEY = "current_playlist"
        fun newInstace():PlayListsFragment{
            val fragment= PlayListsFragment()
            val bundle=Bundle()
            fragment.arguments=bundle
            return fragment
        }
    }
}