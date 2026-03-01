package com.practicum.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding

class PlayListsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonAddNewPlayList.setOnClickListener{
            findNavController().navigate(R.id.action_mediaLibFragment_to_newPlayListFragment)
        }
    }

    companion object{
        fun newInstace():PlayListsFragment{
            val fragment= PlayListsFragment()
            val bundle=Bundle()
            fragment.arguments=bundle
            return fragment
        }
    }
}