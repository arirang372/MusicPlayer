package com.sung.musicplayer.service

import com.sung.musicplayer.model.Song


/**
 * To make an interaction between [SongPlayerService] & [SongPlayerActivity]
 *
 * @author John Sung
 */

interface OnPlayerServiceCallback {

    fun updateSongData(song: Song)

    fun updateSongProgress(duration: Long, position: Long)

    fun setBufferingData(isBuffering: Boolean)

    fun setVisibilityData(isVisibility: Boolean)

    fun setPlayStatus(isPlay: Boolean)

    fun stopService()
}