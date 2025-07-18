package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding

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

        val pushbackbutton=findViewById<Button>(R.id.fromSearchBackToMain)

        pushbackbutton.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearSearchSign)
        val inputEditText = findViewById<EditText>(R.id.inputSearch)


        inputEditText.setText(searchedName)

        clearButton.setOnClickListener {
            inputEditText.setText("")
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
            }
        }
        inputEditText.addTextChangedListener(textInputControl)
    }
}