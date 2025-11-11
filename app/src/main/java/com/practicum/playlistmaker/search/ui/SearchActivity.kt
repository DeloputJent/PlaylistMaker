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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.sharedpreferences.SearchHistory
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.player.ui.MusicPlayerActivity
import com.practicum.playlistmaker.presentation.TrackListAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val tracksInteractor = Creator.provideTracksInteractor()

    var searchedName:String?=""

    lateinit var historyOfSearch: SearchHistory

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MEMMORY, searchedName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchedName = savedInstanceState.getString(MEMMORY, MEMMORY_DEF)
    }

    private lateinit var trackAdapter : TrackListAdapter

    private lateinit var recyclerView : RecyclerView
    private lateinit var handler: Handler

    private val trackList:MutableList<Track> = mutableListOf()
    private var historyList:MutableList<Track> = mutableListOf()

    private val searchRunnable = Runnable { searchThisTrack(binding.inputSearch.text.toString()) }

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

        val displayPlayerIntent= Intent(this@SearchActivity, MusicPlayerActivity::class.java)

        binding.fromSearchBackToMain.setOnClickListener {
            finish()
        }

        binding.inputSearch.setText(searchedName)

        historyOfSearch = Creator.getHistoryOfSearch(this)

        historyList = historyOfSearch.readFromMemory()

        recyclerView = findViewById(R.id.foundedTracksList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        var isClickAllowed = true
        handler = Handler(Looper.getMainLooper())

        fun clickDebounce() : Boolean {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
            }
            return current
        }

        trackAdapter = TrackListAdapter(
            trackList, historyList,
            clickListener = { track ->
                if (clickDebounce()) {
                    historyList.removeIf { it.trackId == track.trackId }
                    if (historyList.size == 10) historyList.removeAt(9)
                    historyList.add(0, track)
                    displayPlayerIntent.putExtra("current_track", track)
                    startActivity(displayPlayerIntent)
                }
            }
        )

        recyclerView.adapter = trackAdapter

        val recyclerViewHistory = findViewById<RecyclerView>(R.id.TracksSearchHistory)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        val trackAdapterHistory = TrackListAdapter(historyList, clickListener = { track ->
            displayPlayerIntent.putExtra("current_track", track)
            startActivity(displayPlayerIntent)
        })

        recyclerViewHistory.adapter = trackAdapterHistory

        binding.clearSearchSign.setOnClickListener {
            binding.inputSearch.setText("")
            hideProblemMessageAndButton()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
        }

        binding.clearHistoryButton.setOnClickListener{
            historyList.clear()
            trackAdapterHistory.notifyDataSetChanged()
            historyOfSearch.clearMemory()
            binding.historyLayout.visibility= View.GONE
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        val textInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchSign.isVisible = !s.isNullOrEmpty()


                searchDebounce()

                if (binding.inputSearch.hasFocus() && s?.isEmpty()==true && historyList.isNotEmpty()) {
                    recyclerView.visibility = View.GONE
                    trackAdapterHistory.notifyDataSetChanged()
                    binding.historyLayout.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    binding.historyLayout.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                searchedName=s.toString()
                if (s != null) {
                    if (s.isEmpty()) {
                        hideProblemMessageAndButton()
                    }
                }
            }
        }

        binding.inputSearch.addTextChangedListener(textInputControl)

        binding.refreshThisSearchButton.setOnClickListener{
            searchThisTrack(binding.inputSearch.text.toString())
        }

        binding.inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputSearch.text.isNotEmpty()) {
                    searchRunnable
                }
                true
            }
            false
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputSearch.text.isEmpty() && historyList.isNotEmpty()) {
                recyclerView.visibility = View.GONE
                trackAdapterHistory.notifyDataSetChanged()
                binding.historyLayout.visibility = View.VISIBLE
            } else {
                binding.historyLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }//OnCreate

    override fun onPause() {
        super.onPause()
        if (historyList.isNotEmpty()) historyOfSearch.writeInMemory(historyList)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    fun searchThisTrack(songName:String) {
        if(songName.isNotEmpty()) {
            hideProblemMessageAndButton()
            recyclerView.visibility= View.GONE
            binding.progressBar.visibility = View.VISIBLE
            tracksInteractor.searchTracks(songName, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        binding.progressBar.visibility = View.GONE
                        if (foundTracks != null) {
                            if (foundTracks.isNotEmpty()) {
                                trackList.clear()
                                trackList.addAll(foundTracks)
                                trackAdapter.notifyDataSetChanged()
                                recyclerView.visibility = View.VISIBLE
                            } else {
                                showNothingFoundMessage()
                            }
                        } else {
                            showNoNetMessageAndButton()
                        }
                    }
                }
            })
        }
    } //fun searchThisTrack()

    fun showNothingFoundMessage() {
        binding.apply {
            errorSign.setImageResource(R.drawable.ic_nothing_found_120)
            searchProblemMessage.setText(R.string.nothing_found)
            errorSign.visibility = View.VISIBLE
            searchProblemMessage.visibility = View.VISIBLE
        }
    }

    fun showNoNetMessageAndButton() {
        binding.apply {
            errorSign.setImageResource(R.drawable.ic_no_connection_120)
            searchProblemMessage.setText(R.string.problem_with_connection)
            errorSign.visibility = View.VISIBLE
            searchProblemMessage.visibility = View.VISIBLE
            refreshThisSearchButton.visibility = View.VISIBLE
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
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}