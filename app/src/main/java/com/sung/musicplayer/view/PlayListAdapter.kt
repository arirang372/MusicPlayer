package com.sung.musicplayer.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sung.musicplayer.databinding.SongListItemBinding
import com.sung.musicplayer.model.Song


class PlayListAdapter(private val callback: PlayListCallback) :
    ListAdapter<Song, MusicSongRecyclerViewHolder>(SongComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MusicSongRecyclerViewHolder(
            SongListItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: MusicSongRecyclerViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, callback) }
    }
}

class MusicSongRecyclerViewHolder(private val binding: SongListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Song, callback: PlayListCallback) {
        binding.model = item
        binding.callback = callback
    }
}

object SongComparator : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem == newItem
}
