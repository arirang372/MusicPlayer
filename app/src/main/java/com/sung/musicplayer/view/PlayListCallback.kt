package com.sung.musicplayer.view

import com.sung.musicplayer.model.Song

interface PlayListCallback {

    fun onListItemLongClick(song: Song)

    fun onListItemClick(song: Song)

    fun onFloatingButtonClick() {
        //do nothing by default
    }

}