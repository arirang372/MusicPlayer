package com.sung.musicplayer.media

import com.sung.musicplayer.model.Song

/**
 * To make an interaction between [ExoPlayerManager] & [MediaController]
 *
 * and to return result from [ExoPlayerManager]
 *
 * @author John Sung
 *
 */
interface OnExoPlayerManagerCallback {

    fun getCurrentStreamPosition(): Long

    fun stop()

    fun play(aSong: Song)

    fun pause()

    fun seekTo(position: Long)

    fun setCallback(callback: OnSongStateCallback)

    /**
     * This class gives the information about current song
     * (position, the state of completion, when it`s changed, ...)
     *
     * */
    interface OnSongStateCallback {

        fun onCompletion()

        fun onPlaybackStatusChanged(state: Int)

        fun setCurrentPosition(position: Long, duration: Long)

        fun getCurrentSong(): Song?

        fun getCurrentSongList(): ArrayList<Song>?

        fun shuffle(isShuffle: Boolean)

        fun repeat(isRepeat: Boolean)

        fun repeatAll(isRepeatAll: Boolean)

    }
}