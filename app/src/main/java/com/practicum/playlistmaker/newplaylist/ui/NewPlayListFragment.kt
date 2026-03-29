package com.practicum.playlistmaker.newplaylist.ui

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.util.TypedValueCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlayListFragment: Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    val binding get() = _binding!!
    private val viewModel: NewPlayListViewModel by viewModel()
    private lateinit var playlistArtwork:ImageView
    var playListName: String = ""
    var playListDescription: String = ""
    var uri: Uri = Uri.EMPTY
    private var nameInputControl: TextWatcher? = null
    private var descriptionInputControl: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistArtwork = binding.PlaylistArtwork

        nameInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.PlaylistNameHint.isVisible = !s.isNullOrEmpty()
                binding.createPlaylistButton.isEnabled = !s.isNullOrEmpty()
                playListName=s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        descriptionInputControl = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.PlaylistDescriptionHint.isVisible = !s.isNullOrEmpty()
                playListDescription=s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputPlaylistName.addTextChangedListener(nameInputControl)
        binding.inputPlaylistDescription.addTextChangedListener(descriptionInputControl)

        binding.backFromNewPlaylistButton.setOnClickListener {
            if (playListName.isEmpty() and playListDescription.isEmpty() and (playlistArtwork.drawable==null)) findNavController().navigateUp()
            else onScreenCloseDialog(requireContext())
        }

        val pickPicture = registerForActivityResult(ActivityResultContracts
            .PickVisualMedia()) { uri->
            if(uri!=null) {
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .transform(
                        RoundedCorners(
                            TypedValueCompat.dpToPx(8f, this.resources.displayMetrics).toInt()
                        )
                    )
                    .placeholder(R.drawable.img_placeholder_312)
                    .into( playlistArtwork)
                this.uri=uri
                playlistArtwork.setImageURI(uri)
            }
        }

        playlistArtwork.setOnClickListener {
            pickPicture.launch(PickVisualMediaRequest(
                ActivityResultContracts
                .PickVisualMedia
                .ImageOnly))
            playlistArtwork.scaleType= ImageView.ScaleType.FIT_XY
        }

        binding.createPlaylistButton.setOnClickListener {
            var path:String=""
            if (uri!=Uri.EMPTY)
            {
                path = viewModel.saveImageToPrivateStorage(uri, playListName)
            }
            viewModel.createPlayList(playListName, playListDescription, path)
            val toast = Toast(requireContext())
            toast.duration= Toast.LENGTH_SHORT
            toast.setText(getString(R.string.playlist_created, playListName))
            toast.show()
            findNavController().navigateUp()
        }

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isDescriptionEmpty()) onScreenCloseDialog(requireContext())
                else findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nameInputControl?.let { binding.inputPlaylistName.removeTextChangedListener(it) }
        descriptionInputControl?.let{binding.inputPlaylistDescription.removeTextChangedListener(it)}
        _binding = null
    }

    fun isDescriptionEmpty(): Boolean {
       return playListName.isEmpty() and playListDescription.isEmpty() and (playlistArtwork.drawable==null)
    }
    private fun onScreenCloseDialog(context: Context) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.Finish_creating_a_playlist)
            .setMessage(R.string.Unsaved_data_will_be_lost)
            .setNegativeButton(R.string.Cancel) { dialog, which ->{}
            }.setPositiveButton(R.string.Complete) { dialog, which -> findNavController().navigateUp()
            }.create()

        dialog.setOnShowListener {
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.Black_white_color))

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.Black_white_color))
        }
        dialog.show()
    }
}
