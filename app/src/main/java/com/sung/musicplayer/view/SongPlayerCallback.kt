package com.sung.musicplayer.view

interface SongPlayerCallback {

    fun onNextClick()
    fun onPrevClick()
    fun onToggleClick()
    fun onProgressChanged(progress: Int, fromUser: Boolean)
}