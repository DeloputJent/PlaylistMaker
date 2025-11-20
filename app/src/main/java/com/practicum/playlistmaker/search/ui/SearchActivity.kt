package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.player.ui.MusicPlayerActivity
import com.practicum.playlistmaker.search.ui.presentation.TrackListAdapter
import com.practicum.playlistmaker.search.domain.SearchTrackState

class SearchActivity : AppCompatActivity() {

    companion object {
        const val MEMMORY = ""
        const val MEMMORY_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MEMMORY, searchedName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchedName = savedInstanceState.getString(MEMMORY, MEMMORY_DEF)
    }

    private lateinit var binding: ActivitySearchBinding
    private var viewModel: SearchViewModel? = null
    private var textInputControl: TextWatcher? = null
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    var searchedName:String?=""
    private lateinit var trackAdapter : TrackListAdapter
    private lateinit var trackAdapterHistory : TrackListAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerViewHistory : RecyclerView
    lateinit var displayPlayerIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        binding.fromSearchBackToMain.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        viewModel?.readFromMemory()

        displayPlayerIntent = Intent(this@SearchActivity, MusicPlayerActivity::class.java)

        binding.inputSearch.setText(searchedName)

        recyclerView = findViewById(R.id.foundedTracksList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        trackAdapter = TrackListAdapter(
            clickListener = { track ->
                if (clickDebounce()) {
                    viewModel?.addToHistoryList(track)
                    displayPlayerIntent.putExtra("current_track", track)
                    startActivity(displayPlayerIntent)
                }
            }
        )

        recyclerView.adapter = trackAdapter

        recyclerViewHistory = findViewById<RecyclerView>(R.id.TracksSearchHistory)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)

       trackAdapterHistory = TrackListAdapter(clickListener = { track ->
            displayPlayerIntent.putExtra("current_track", track)
            startActivity(displayPlayerIntent)
       })

        recyclerViewHistory.adapter = trackAdapterHistory

        binding.clearSearchSign.setOnClickListener {
            binding.inputSearch.setText("")
            recyclerView.visibility= View.GONE
            hideProblemMessageAndButton()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
        }

        binding.clearHistoryButton.setOnClickListener{
            viewModel?.clearHistory()
            binding.historyLayout.visibility= View.GONE
        }

        binding.refreshThisSearchButton.setOnClickListener{
            viewModel?.searchDebounce("")
            viewModel?.searchDebounce(binding.inputSearch.text.toString())
        }

        textInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchSign.isVisible = !s.isNullOrEmpty()
                viewModel?.searchDebounce(changedText = s?.toString() ?: "")
                if (binding.inputSearch.hasFocus() && s?.isEmpty()==true) {
                    viewModel?.showHistory()
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
                    viewModel?.searchDebounce(binding.inputSearch.text.toString())
                }
                true
            }
            false
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputSearch.text.isEmpty()) {
                viewModel?.showHistory()
            } else {
                binding.historyLayout.visibility = View.GONE
            }
        }
    }//OnCreate

    override fun onPause() {
        super.onPause()
        viewModel?.writeInMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
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
}