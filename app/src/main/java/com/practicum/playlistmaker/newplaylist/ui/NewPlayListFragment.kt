package com.practicum.playlistmaker.newplaylist.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlayListFragment: Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel: NewPlayListViewModel by viewModel()

    private var nameInputControl: TextWatcher? = null
    private var descriptionInputControl: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.PlaylistNameHint.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        descriptionInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.PlaylistDescriptionHint.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.inputPlaylistName.addTextChangedListener(nameInputControl)
        binding.inputPlaylistDescription.addTextChangedListener(descriptionInputControl)

        binding.backFromNewPlaylistButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nameInputControl?.let { binding.inputPlaylistName.removeTextChangedListener(it) }
        descriptionInputControl?.let{binding.inputPlaylistDescription.removeTextChangedListener(it)}
    }

    companion object{
    }
}
