package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.MusicPlayerActivity
import com.practicum.playlistmaker.search.domain.SearchTrackState
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.presentation.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MEMMORY, searchedName)
    }

    private val viewModel:SearchViewModel by viewModel()
    private var textInputControl: TextWatcher? = null
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    var searchedName:String?=""
    private lateinit var trackAdapter : TrackListAdapter
    private lateinit var trackAdapterHistory : TrackListAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerViewHistory : RecyclerView
    lateinit var displayPlayerIntent : Intent

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.readFromMemory()

        displayPlayerIntent = Intent(requireContext(), MusicPlayerActivity::class.java)

        binding.inputSearch.setText(searchedName)

        recyclerView = binding.foundedTracksList

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        trackAdapter = TrackListAdapter(
            clickListener = { track ->
                if (clickDebounce()) {
                    viewModel.addToHistoryList(track)
                    displayPlayerIntent.putExtra("current_track", track)
                    startActivity(displayPlayerIntent)
                }
            }
        )

        recyclerView.adapter = trackAdapter

        recyclerViewHistory = binding.TracksSearchHistory
        recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())

        trackAdapterHistory = TrackListAdapter(clickListener = { track ->
            displayPlayerIntent.putExtra("current_track", track)
            startActivity(displayPlayerIntent)
        })

        recyclerViewHistory.adapter = trackAdapterHistory

        binding.clearSearchSign.setOnClickListener {
            binding.inputSearch.setText("")
            recyclerView.visibility= View.GONE
            hideProblemMessageAndButton()
            val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
        }

        binding.clearHistoryButton.setOnClickListener{
            viewModel.clearHistory()
            binding.historyLayout.visibility= View.GONE
        }

        binding.refreshThisSearchButton.setOnClickListener{
            viewModel.searchDebounce("")
            viewModel.searchDebounce(binding.inputSearch.text.toString())
        }

        textInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchSign.isVisible = !s.isNullOrEmpty()
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
                if (binding.inputSearch.hasFocus() && s?.isEmpty()==true) {
                    viewModel.showHistory()
                } else {
                    binding.historyLayout.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        }

        binding.inputSearch.addTextChangedListener(textInputControl)

        binding.inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputSearch.text.isNotEmpty()) {
                    viewModel.searchDebounce(binding.inputSearch.text.toString())
                }
                true
            }
            false
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputSearch.text.isEmpty()) {
                viewModel.showHistory()
            } else {
                binding.historyLayout.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        textInputControl?.let { binding.inputSearch.removeTextChangedListener(it) }
    }


    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun render(state: SearchTrackState) {
        when (state) {
            is SearchTrackState.Loading -> showLoading()
            is SearchTrackState.Content -> showContent(state.tracks)
            is SearchTrackState.ShowHistory -> showHistory(state.tracks)
            is SearchTrackState.NoNetFound -> showNoNetMessageAndButton()
            is SearchTrackState.NothingFound -> showNothingFoundMessage()
        }
    }

    fun showLoading() {
        hideProblemMessageAndButton()
        recyclerView.visibility= View.GONE
        binding.historyLayout.visibility= View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun showContent(tracks: List<Track>) {
        hideProblemMessageAndButton()
        binding.progressBar.visibility = View.GONE
        binding.historyLayout.visibility= View.GONE
        trackAdapter.setTrackList(tracks)
        recyclerView.visibility= View.VISIBLE
    }

    fun showHistory(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            hideProblemMessageAndButton()
            binding.progressBar.visibility = View.GONE
            recyclerView.visibility= View.GONE
            trackAdapterHistory.setTrackList(tracks)
            binding.historyLayout.visibility= View.VISIBLE
        } else binding.historyLayout.visibility= View.GONE
    }

    fun showNothingFoundMessage() {
        binding.apply {
            binding.progressBar.visibility = View.GONE
            errorSign.visibility = View.VISIBLE
            searchProblemMessage.visibility = View.VISIBLE
            errorSign.setImageResource(R.drawable.ic_nothing_found_120)
            searchProblemMessage.setText(R.string.nothing_found)
        }
    }

    fun showNoNetMessageAndButton() {
        binding.apply {
            binding.progressBar.visibility = View.GONE
            searchProblemMessage.visibility = View.VISIBLE
            refreshThisSearchButton.visibility = View.VISIBLE
            errorSign.visibility = View.VISIBLE
            errorSign.setImageResource(R.drawable.ic_no_connection_120)
            searchProblemMessage.setText(R.string.problem_with_connection)
        }
    }

    fun hideProblemMessageAndButton() {
        binding.apply {
            errorSign.visibility = View.GONE
            searchProblemMessage.visibility = View.GONE
            refreshThisSearchButton.visibility = View.GONE
        }
    }

    companion object {
        const val MEMMORY = ""
        const val MEMMORY_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}