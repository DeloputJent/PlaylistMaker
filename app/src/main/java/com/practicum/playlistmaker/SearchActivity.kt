package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
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
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    var searchedName:String?=""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MEMMORY, searchedName)
    }
    companion object {
        const val MEMMORY = ""
        const val MEMMORY_DEF = ""
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

    private val trackList = ArrayList<Track>()

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

        val iTunesService=NetWorkClient()

        pushbackbutton=findViewById(R.id.fromSearchBackToMain)
        clearButton = findViewById(R.id.clearSearchSign)
        inputEditText = findViewById(R.id.inputSearch)
        errorSign = findViewById(R.id.errorSign)
        searchProblemMessage = findViewById(R.id.searchProblemMessage)
        refreshThisSearchButton = findViewById(R.id.refreshThisSearchButton)

        pushbackbutton.setOnClickListener {
            finish()
        }

        inputEditText.setText(searchedName)

        val recyclerView = findViewById<RecyclerView>(R.id.foundedTracksList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val trackAdapter = TrackListAdapter(trackList)
        recyclerView.adapter = trackAdapter

        clearButton.setOnClickListener {
            inputEditText.setText("")
            errorSign.visibility = GONE
            searchProblemMessage.visibility = GONE
            refreshThisSearchButton.visibility = GONE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val textInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {
                searchedName=s.toString()
                if (s != null) {
                    if (s.isNullOrEmpty()) {
                        errorSign.visibility = GONE
                        searchProblemMessage.visibility = GONE
                        refreshThisSearchButton.visibility = GONE
                    }
                }
            }
        }

        inputEditText.addTextChangedListener(textInputControl)

        fun searchThisTrack(songName:String) {
            iTunesService.Service.search(songName)
                .enqueue(object : Callback<iTunesResponse> {
                    override fun onResponse(call: Call<iTunesResponse>,
                                            response: Response<iTunesResponse>) {
                        errorSign.visibility = GONE
                        searchProblemMessage.visibility = GONE
                        refreshThisSearchButton.visibility = GONE
                        trackList.clear()
                        trackAdapter.notifyDataSetChanged()
                        if (response.code() == 200) {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                errorSign.setImageResource(R.drawable.ic_nothing_found_120)
                                searchProblemMessage.setText(R.string.nothing_found)
                                errorSign.visibility = VISIBLE
                                searchProblemMessage.visibility = VISIBLE
                            }
                        } else {
                            errorSign.setImageResource(R.drawable.ic_no_connection_120)
                            errorSign.visibility = VISIBLE
                            searchProblemMessage.setText(R.string.problem_with_connection)
                            searchProblemMessage.visibility = VISIBLE
                            refreshThisSearchButton.visibility = VISIBLE
                        }
                    }//onResponse

                    override fun onFailure(call: Call<iTunesResponse>, t: Throwable) {
                        trackList.clear()
                        trackAdapter.notifyDataSetChanged()
                        errorSign.setImageResource(R.drawable.ic_no_connection_120)
                        errorSign.visibility = VISIBLE
                        searchProblemMessage.setText(R.string.problem_with_connection)
                        searchProblemMessage.visibility = VISIBLE
                        refreshThisSearchButton.visibility = VISIBLE
                    } //onFailure
                })
        } //fun searchThisTrack()

        refreshThisSearchButton.setOnClickListener{
            searchThisTrack(inputEditText.text.toString())
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    searchThisTrack(inputEditText.text.toString())
                }
                true
            }
            false
        }
    }//OnCreate
}