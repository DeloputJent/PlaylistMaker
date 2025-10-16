package com.practicum.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharedpreferences.SearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.presentation.TrackListAdapter

class SearchActivity : AppCompatActivity() {
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

    private lateinit var pushbackbutton:Button
    private lateinit var clearButton:ImageView
    private lateinit var inputEditText:EditText
    private lateinit var errorSign:ImageView
    private lateinit var searchProblemMessage:TextView
    private lateinit var refreshThisSearchButton:Button
    private lateinit var historyOfSearchView:LinearLayout
    private lateinit var clearHistoryOfSearchButton:Button
    private lateinit var progressBar: ProgressBar
    private lateinit var trackAdapter : TrackListAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var handler:Handler

    private val trackList:MutableList<Track> = mutableListOf()
    private var historyList:MutableList<Track> = mutableListOf()

    private val searchRunnable = Runnable { searchThisTrack(inputEditText.text.toString()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val currentView=findViewById<View>(R.id.search)

        ViewCompat.setOnApplyWindowInsetsListener(currentView) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationBar.bottom)
            view.updatePadding(top = statusBar.top)
            insets
        }

        pushbackbutton=findViewById(R.id.fromSearchBackToMain)
        clearButton = findViewById(R.id.clearSearchSign)
        inputEditText = findViewById(R.id.inputSearch)
        errorSign = findViewById(R.id.errorSign)
        searchProblemMessage = findViewById(R.id.searchProblemMessage)
        refreshThisSearchButton = findViewById(R.id.refreshThisSearchButton)
        progressBar=findViewById(R.id.progressBar)
        clearHistoryOfSearchButton = findViewById(R.id.clearHistoryButton)
        historyOfSearchView = findViewById(R.id.historyLayout)

        val displayPlayerIntent= Intent(this@SearchActivity, MusicPlayerActivity::class.java)

        pushbackbutton.setOnClickListener {
            finish()
        }

        inputEditText.setText(searchedName)

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

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideProblemMessageAndButton()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        clearHistoryOfSearchButton.setOnClickListener{
            historyList.clear()
            trackAdapterHistory.notifyDataSetChanged()
            historyOfSearch.clearMemory()
            historyOfSearchView.visibility=GONE
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        val textInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()

                searchDebounce()

                if (inputEditText.hasFocus() && s?.isEmpty()==true && historyList.isNotEmpty()) {
                    recyclerView.visibility = GONE
                    trackAdapterHistory.notifyDataSetChanged()
                    historyOfSearchView.visibility = VISIBLE
                } else {
                    recyclerView.visibility = VISIBLE
                    historyOfSearchView.visibility = GONE
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

        inputEditText.addTextChangedListener(textInputControl)

        refreshThisSearchButton.setOnClickListener{
                searchThisTrack(inputEditText.text.toString())
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    searchRunnable
                }
                true
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && inputEditText.text.isEmpty() && historyList.isNotEmpty()) {
                    recyclerView.visibility = GONE
                    trackAdapterHistory.notifyDataSetChanged()
                    historyOfSearchView.visibility = VISIBLE
                } else {
                    recyclerView.visibility = VISIBLE
                    historyOfSearchView.visibility = GONE
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
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            progressBar.visibility = VISIBLE
            tracksInteractor.searchTracks(songName, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        progressBar.visibility = GONE
                        trackList.clear()
                        if (foundTracks != null) {
                            if (foundTracks.isNotEmpty()) {
                                trackList.addAll(foundTracks)
                            }
                            if (trackList.isNotEmpty()) {
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                progressBar.visibility = GONE
                                trackList.clear()
                                trackAdapter.notifyDataSetChanged()
                                showNothingFoundMessage()
                            }
                        } else {
                            progressBar.visibility = GONE
                            trackList.clear()
                            trackAdapter.notifyDataSetChanged()
                            showNoNetMessageAndButton()
                        }
                    }
                }
            })
        }
    } //fun searchThisTrack()

    fun showNothingFoundMessage() {
        errorSign.setImageResource(R.drawable.ic_nothing_found_120)
        searchProblemMessage.setText(R.string.nothing_found)
        errorSign.visibility = VISIBLE
        searchProblemMessage.visibility = VISIBLE
    }

    fun showNoNetMessageAndButton() {
        errorSign.setImageResource(R.drawable.ic_no_connection_120)
        searchProblemMessage.setText(R.string.problem_with_connection)
        errorSign.visibility = VISIBLE
        searchProblemMessage.visibility = VISIBLE
        refreshThisSearchButton.visibility = VISIBLE
    }

    fun hideProblemMessageAndButton() {
        errorSign.visibility = GONE
        searchProblemMessage.visibility = GONE
        refreshThisSearchButton.visibility = GONE
    }

    companion object {
        const val MEMMORY = ""
        const val MEMMORY_DEF = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}