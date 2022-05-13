package com.sung.musicplayer.media

import com.sung.musicplayer.model.Song


/**
 * To return the result of [MediaAdapter]
 *
 * and also to make an interaction between [SongPlayerService] & [MediaAdapter]
 *
 * @author John Sung
 *
 */
interface OnMediaAdapterCallback {

    fun addNewPlaylistToCurrent(songList: ArrayList<Song>)

    fun onPlaybackStateChanged(state: Int)

    fun onRepeat(isRepeat: Boolean)

    fun onRepeatAll(repeatAll: Boolean)

    fun onShuffle(isShuffle: Boolean)

    fun onSongChanged(song: Song)

    fun setDuration(duration: Long, position: Long)
}