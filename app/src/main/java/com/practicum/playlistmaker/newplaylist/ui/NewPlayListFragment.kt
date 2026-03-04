package com.practicum.playlistmaker.newplaylist.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlayListFragment: Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel: NewPlayListViewModel by viewModel()
    private lateinit var playListName: String
    private lateinit var playListDescription: String

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
                binding.createPlaylistButton.isEnabled = !s.isNullOrEmpty()
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

        val pickPicture = registerForActivityResult(ActivityResultContracts
            .PickVisualMedia()) { uri->
            if(uri!=null) { binding.PlaylistArtwork.setImageURI(uri) }
        }

        binding.PlaylistArtwork.setOnClickListener {
            pickPicture.launch(PickVisualMediaRequest(
                ActivityResultContracts
                .PickVisualMedia
                .ImageOnly))
            binding.PlaylistArtwork.scaleType= ImageView.ScaleType.FIT_XY
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
