package com.sung.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.sung.musicplayer.media.ExoPlayerManager
import com.sung.musicplayer.media.MediaAdapter
import com.sung.musicplayer.media.OnMediaAdapterCallback
import com.sung.musicplayer.media.PlaybackState
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.notification.MediaNotificationManager

class SongPlayerService : Service(), OnMediaAdapterCallback {

    private var mMediaAdapter: MediaAdapter? = null
    private var mNotificationManager: MediaNotificationManager? = null
    private val binder = LocalBinder()
    private var playState = 0
    var mCallback: OnPlayerServiceCallback? = null
    var command: String? = null

    override fun onCreate() {
        super.onCreate()
        val exoPlayerManager = ExoPlayerManager(this)
        mMediaAdapter = MediaAdapter(exoPlayerManager, this)
        mNotificationManager = MediaNotificationManager(this)
        mNotificationManager?.createMediaNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,
            "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId")
        return START_NOT_STICKY
    }

    fun subscribeToSongPlayerUpdates() {
        Log.d(TAG, "subscribeToSongPlayerUpdates() called")
        /* Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        * ensure this Service can be promoted to a foreground service.
        */
        ContextCompat.startForegroundService(applicationContext,
            Intent(this, SongPlayerService::class.java))
    }

    fun addListener(callback: OnPlayerServiceCallback) {
        mCallback = callback
    }

    fun removeListener() {
        mCallback = null
    }

    fun getCurrentSong() = mMediaAdapter?.getCurrentSong()

    fun getCurrentSongList() = mMediaAdapter?.getCurrentSongList()

    fun getPlayState() = playState

    private fun play(song: Song?) {
        song?.let {
            mMediaAdapter?.play(song)
        }
    }

    fun play(songList: MutableList<Song>?, song: Song?) {
        song?.let { nonNullSong ->
            songList?.let {
                mMediaAdapter?.play(it, nonNullSong) ?: play(nonNullSong)
            }
        }
    }

    fun playCurrentSong() {
        getCurrentSong()?.let { play(it) }
    }

    fun pause() {
        mMediaAdapter?.pause()
    }

    fun stop() {
        mMediaAdapter?.stop()
        stopForeground(true)
        mNotificationManager = null
        stopSelf()
        mCallback?.stopService()
    }

    override fun addNewPlaylistToCurrent(songList: ArrayList<Song>) {
        mMediaAdapter?.addToCurrentPlaylist(songList)
    }

    override fun onSongChanged(song: Song) {
        mCallback?.updateSongData(song)
    }

    override fun onPlaybackStateChanged(state: Int) {
        playState = state
        when (state) {
            PlaybackState.STATE_BUFFERING -> {
                mCallback?.setBufferingData(true)
                mCallback?.setVisibilityData(true)
            }
            PlaybackState.STATE_PLAYING -> {
                mCallback?.setBufferingData(false)
                mCallback?.setVisibilityData(true)
            }

            PlaybackState.STATE_PAUSED -> {
                mCallback?.setBufferingData(false)
                mCallback?.setVisibilityData(true)
            }
            else -> {
                mCallback?.setBufferingData(false)
                mCallback?.setVisibilityData(false)
            }
        }
        mNotificationManager?.generateNotification()
    }

    override fun onRepeat(isRepeat: Boolean) {
        mMediaAdapter?.repeat(isRepeat)
    }

    override fun onRepeatAll(repeatAll: Boolean) {
        mMediaAdapter?.repeatAll(repeatAll)
    }

    override fun onShuffle(isShuffle: Boolean) {
        mMediaAdapter?.shuffle(isShuffle)
    }

    override fun setDuration(duration: Long, position: Long) {
        mCallback?.updateSongProgress(duration, position)
    }

    fun skipToNext() {
        mMediaAdapter?.skipToNext()
    }

    fun skipToPrevious() {
        mMediaAdapter?.skipToPrevious()
    }

    fun seekTo(position: Long) {
        mMediaAdapter?.seekTo(position)
    }

    private fun unsubscribeToSongPlayerUpdates() {
        Log.d(TAG, "unsubscribeToSongPlayerUpdates() called")
        removeListener()
    }

    override fun onDestroy() {
        unsubscribeToSongPlayerUpdates()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        val action = intent.action
        command = intent.getStringExtra(CMD_NAME)
        if (ACTION_CMD == action && CMD_PAUSE == command) {
            //TODO:: fill this in..
        }
        return binder
    }

    inner class LocalBinder : Binder() {
        val service: SongPlayerService
            get() = this@SongPlayerService
    }

    companion object {
        private val TAG = SongPlayerService::class.java.name
        const val ACTION_CMD = "app.ACTION_CMD"
        const val CMD_NAME = "CMD_NAME"
        const val CMD_PAUSE = "CMD_PAUSE"
    }
}