package com.practicum.playlistmaker.playlist.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCurrentPlaylistBinding
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.player.ui.MusicPlayerFragment
import com.practicum.playlistmaker.playlist.ui.presentation.TracksInPlaylistAdapter
import com.practicum.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlayListFragment : Fragment() {
    private val viewModel by viewModel<PlayListViewModel>()
    private var _binding: FragmentCurrentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playListAdapter : TracksInPlaylistAdapter

    val bundle = Bundle()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentPlaylistBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPlaylistId = requireArguments().getInt(PLAYLIST_KEY)

        viewModel.getPlaylistsById(currentPlaylistId)

        viewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) {
            render(it.playList, it.tracks, it.summaryTime, it.tracksAmount)
            viewModel.setTrackSet(it.tracks)
        }

        viewModel.isPlaylistDeleted.observe(viewLifecycleOwner) {
            isDeleted -> if (isDeleted) {
                findNavController().navigateUp()
            }
        }

        binding.backFromPlaylistButton.setOnClickListener{
            findNavController().navigateUp()
        }

        val bottomSheetContainer = binding.bottomSheet

        val bottomSheetContainerOptions = binding.bottomSheetOptions

        val bottomSheetBehavior = BottomSheetBehavior
            .from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        val bottomSheetBehaviorOptions = BottomSheetBehavior
            .from(bottomSheetContainerOptions).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        var recyclerView = binding.tracksInPlaylist

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        playListAdapter = TracksInPlaylistAdapter(clickListener = { track ->
            bundle.putParcelable(CURRENT_TRACK, track)
            val fragment = MusicPlayerFragment()
            fragment.arguments = bundle
            findNavController().navigate(
            R.id.action_playListFragment_to_musicPlayerFragment,
            MusicPlayerFragment.createArgs(track))
        },
            longClickListener = {
                track -> deleteThisTrackDialog(requireContext(), track)
            }
        )

        recyclerView.adapter = playListAdapter

        binding.sharePlaylist.setOnClickListener {
            if(viewModel.currentPlaylist.tracksAmount==0) {
                nothingToShareMessage(requireContext())
            } else {
            viewModel.sharePlaylist(binding.TracksAmount.text.toString())
            }
        }

        val overlay = binding.overlay

        bottomSheetBehaviorOptions.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            overlay.visibility = View.GONE
                        }
                        else -> {
                            overlay.visibility = View.VISIBLE
                        }
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }
        )

        binding.playlistOptions.setOnClickListener {
            bottomSheetBehaviorOptions.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.OptionSharePlaylist.setOnClickListener {
            if(viewModel.currentPlaylist.tracksAmount==0) {
                nothingToShareMessage(requireContext())
            } else {
                viewModel.sharePlaylist(binding.TracksAmount.text.toString())
            }
        }

        binding.OptionModifyPlaylist.setOnClickListener {
            bundle.putInt(PLAYLIST_KEY, currentPlaylistId)
            findNavController().navigate(
                R.id.action_playListFragment_to_modifyPlayListFragment,bundle
            )
        }

        binding.OptionDeletePlaylist.setOnClickListener {
            deleteThisPlaylistDialog(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun nothingToShareMessage (context: Context)
    {
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.setText(R.string.You_cant_share_this_playlist)
        toast.show()
    }

    fun render(playlist: Playlist, tracks: List<Track>, summaryTime:Int, tracksAmount: Int) {

        val uri=viewModel.getImageUri()

        Glide.with(this)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(
                dpToPx(8f, this.resources.displayMetrics).toInt()
            ))
            .placeholder(R.drawable.placeholder)
            .into(binding.PlaylistArtwork)

        Glide.with(this)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(
                dpToPx(2f, this.resources.displayMetrics).toInt()
            ))
            .placeholder(R.drawable.placeholder)
            .into(binding.OptionPlayListCover)

        binding.apply {
            PlaylistName.text = playlist.playlistName
            PlaylistDescription.text = playlist.playlistDescription
            SummaryLength.text = requireContext()
                .resources
                .getQuantityString(R.plurals.summary_time,summaryTime,summaryTime)
            TracksAmount.text = requireContext()
                .resources
                .getQuantityString(R.plurals.tracks,tracksAmount,tracksAmount)
            OptionPlayListName.text = playlist.playlistName
            OptionPlayListSongsAmount.text = requireContext()
                .resources
                .getQuantityString(R.plurals.tracks,tracksAmount,tracksAmount)
        }
        showTracks(tracks)
    }

    private fun deleteThisTrackDialog(context: Context, track: Track,) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.WhiteDialogTheme)
            .setTitle(R.string.Do_you_want_delete_track)
            .setNegativeButton(R.string.No_answer) { dialog, which ->{}
            }.setPositiveButton(R.string.Yes_answer) { dialog, which -> run {
                viewModel.deleteTrackFromPlaylist(track)
            }
            }.create()

        dialog.setOnShowListener {
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.YP_Blue))

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.YP_Blue))
        }
        dialog.show()
    }

    private fun deleteThisPlaylistDialog(context: Context) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.WhiteDialogTheme)
            .setTitle(getString(R.string.Do_you_want_delete_playlist, viewModel.currentPlaylist.playlistName))
            .setNegativeButton(R.string.No_answer) { dialog, which ->{}
            }.setPositiveButton(R.string.Yes_answer) { dialog, which -> run {
                viewModel.deleteThisPlaylist()
            }
            }.create()

        dialog.setOnShowListener {
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.YP_Blue))

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.YP_Blue))
        }
        dialog.show()
    }


    fun showTracks(tracks: List<Track>) {
        playListAdapter.setTrackList(tracks)
        binding.tracksInPlaylist.visibility= View.VISIBLE
    }

    companion object{
        private const val CURRENT_TRACK = "current_track"
        private const val PLAYLIST_KEY = "current_playlist"

        fun createArgs(currentPlaylistId: Int): Bundle =
            bundleOf(PLAYLIST_KEY to currentPlaylistId,
            )
    }
}