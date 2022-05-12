package com.sung.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SongPlayerService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    companion object {

        const val ACTION_CMD = "app.ACTION_CMD"
        const val CMD_NAME = "CMD_NAME"
        const val CMD_PAUSE = "CMD_PAUSE"
    }
}