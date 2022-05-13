package com.sung.musicplayer.service


/**
 * To make an interaction between [SongPlayerService] & [SongPlayerActivity]
 *
 * @author John Sung
 */

interface OnPlayerServiceCallback {

    fun updateSongProgress(duration: Long, position: Long)

    fun setBufferingData(isBuffering: Boolean)

    fun setVisibilityData(isVisibility: Boolean)

    fun setPlayStatus(isPlay: Boolean)

    fun stopService()
}