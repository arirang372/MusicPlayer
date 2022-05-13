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

    fun onSongChanged(song: Song)

    fun onPlaybackStateChanged(state: Int)

    fun setDuration(duration: Long, position: Long)

    fun addNewPlaylistToCurrent(songList: ArrayList<Song>)

    fun onShuffle(isShuffle: Boolean)

    fun onRepeat(isRepeat: Boolean)

    fun onRepeatAll(repeatAll: Boolean)

}