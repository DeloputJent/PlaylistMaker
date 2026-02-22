package com.practicum.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.medialib.ui.presentation.FavoriteTrackState
import com.practicum.playlistmaker.medialib.ui.presentation.FavoriteListAdapter
import com.practicum.playlistmaker.medialib.ui.presentation.FavoriteViewModel
import com.practicum.playlistmaker.player.ui.MusicPlayerFragment
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoriteViewModel>()
    private lateinit var binding: FragmentFavoriteTracksBinding
    private lateinit var favoriteTrackAdapter : FavoriteListAdapter
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.favoriteTracksList

        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        favoriteTrackAdapter = FavoriteListAdapter(
            clickListener = { track ->
                if (clickDebounce()) {
                    val bundle = Bundle()
                    bundle.putParcelable(CURRENT_TRACK, track)
                    val fragment = MusicPlayerFragment()
                    fragment.arguments = bundle
                    findNavController().navigate(
                        R.id.action_mediaLibFragment_to_musicPlayerFragment,
                        MusicPlayerFragment.createArgs(track)
                    )
                }
            }
        )
        recyclerView.adapter = favoriteTrackAdapter

        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    fun render(state: FavoriteTrackState) {
        when (state) {
            is FavoriteTrackState.Content -> showContent(state.tracks)
            is FavoriteTrackState.Loading -> showLoading()
            is FavoriteTrackState.NothingFound -> showNothingFoundMessage()
        }
    }

    fun showLoading() {
        hideProblemMessage()
        recyclerView.visibility= View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun showContent(tracks: List<Track>) {
        hideProblemMessage()
        binding.progressBar.visibility = View.GONE
        favoriteTrackAdapter.setTrackList(tracks)
        recyclerView.visibility= View.VISIBLE
    }
    fun showNothingFoundMessage() {
        binding.apply {
            binding.progressBar.visibility = View.GONE
            binding.noFavoriteTracksMessage.visibility = View.VISIBLE
        }
    }
    fun hideProblemMessage() {
        binding.noFavoriteTracksMessage.visibility = View.GONE
    }
    private val debounceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + debounceJob)

    var isClickAllowed = true

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            scope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }


    companion object{
        private const val CURRENT_TRACK = "current_track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstace(): FavoriteTracksFragment{
            val fragment= FavoriteTracksFragment()
            val bundle=Bundle()
            fragment.arguments=bundle
            return fragment
        }
    }
}