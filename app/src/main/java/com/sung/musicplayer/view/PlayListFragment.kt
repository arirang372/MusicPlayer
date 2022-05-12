package com.sung.musicplayer.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sung.musicplayer.R
import com.sung.musicplayer.databinding.FragmentPlayListBinding
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.view.SongPlayerActivity.Companion.SONG_ID
import com.sung.musicplayer.viewmodel.PlayListViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PlayListFragment : DaggerFragment(R.layout.fragment_play_list), PlayListCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val playListViewModel: PlayListViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: FragmentPlayListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayListBinding.inflate(inflater, container, false).apply {
            this.viewModel = playListViewModel
            this.callback = this@PlayListFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSong()
    }

    private fun observeSong() {
        playListViewModel.songs.observe(viewLifecycleOwner) {
            binding.playlistRecyclerView.adapter = PlayListAdapter(callback = this).apply {
                submitList(it)
            }
        }
    }

    override fun onListItemLongClick(song: Song) {
        AlertDialog.Builder(requireActivity())
            .setMessage("Are you sure to remove this song?")
            .apply {
                setPositiveButton(R.string.yes) { _, _ ->
                    playListViewModel.removeSong(song)
                }
                setNegativeButton(R.string.no) { _, _ ->
                }
            }
            .show()
    }

    override fun onListItemClick(song: Song) {
        startActivity(Intent(requireActivity(), SongPlayerActivity::class.java).apply {
            putExtra(SONG_ID, song.id)
        })
    }

    override fun onFloatingButtonClick() {
        playListViewModel.onFloatingButtonClick(requireActivity())
    }
}