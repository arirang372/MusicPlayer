package com.sung.musicplayer.media

import android.util.Log
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.playlist.PlaylistManager


/**
 * This class is used to interact with [ExoPlayerManager] & [PlaylistManager]
 *
 * @author John Sung
 */
class MediaAdapter(
    private val onExoPlayerManagerCallback: OnExoPlayerManagerCallback,
    private val mediaAdapterCallback: OnMediaAdapterCallback,
) : OnExoPlayerManagerCallback.OnSongStateCallback, PlaylistManager.OnSongUpdateListener {

    private var playlistManager: PlaylistManager? = null

    init {
        onExoPlayerManagerCallback.setCallback(this)
        playlistManager = PlaylistManager(this)
    }

    fun play(song: Song) {
        onExoPlayerManagerCallback.play(song)
    }

    fun play(songList: MutableList<Song>, song: Song) {
        playlistManager?.setCurrentPlaylist(songList, song)
    }

    fun pause() {
        onExoPlayerManagerCallback.pause()
    }

    fun seekTo(position: Long) {
        onExoPlayerManagerCallback.seekTo(position)
    }

    fun stop() {
        onExoPlayerManagerCallback.stop()
    }

    fun skipToNext() {
        playlistManager?.skipPosition(1)
    }

    fun skipToPrevious() {
        playlistManager?.skipPosition(-1)
    }

    fun addToCurrentPlaylist(songList: ArrayList<Song>) {
        Log.d(TAG, "addToCurrentPlaylist() called with: songList = $songList")
        playlistManager?.addToPlaylist(songList)
    }

    override fun shuffle(isShuffle: Boolean) {
        playlistManager?.setShuffle(isShuffle)
    }

    override fun repeatAll(isRepeatAll: Boolean) {
        playlistManager?.setRepeatAll(isRepeatAll)
    }

    override fun repeat(isRepeat: Boolean) {
        playlistManager?.setRepeat(isRepeat)
    }


    override fun onSongChanged(song: Song) {
        play(song)
        mediaAdapterCallback.onSongChanged(song)
    }

    override fun onSongRetrieveError() {
        //Log.d(TAG, "onSongRetrieveError() called")
    }

    override fun onCompletion() {
        if (playlistManager?.isRepeat() == true) {
            onExoPlayerManagerCallback.stop()
            playlistManager?.repeat()
            return
        }

        if (playlistManager?.hasNext() == true) {
            playlistManager?.skipPosition(1)
            return
        }

        if (playlistManager?.isRepeatAll() == true) {
            playlistManager?.skipPosition(-1)
            return
        }

        onExoPlayerManagerCallback.stop()
    }

    override fun onPlaybackStatusChanged(state: Int) {
        mediaAdapterCallback.onPlaybackStateChanged(state)
    }

    override fun getCurrentSongList(): java.util.ArrayList<Song>? {
        return playlistManager?.getCurrentSongList()
    }

    override fun getCurrentSong(): Song? {
        return playlistManager?.getCurrentSong()
    }

    override fun setCurrentPosition(position: Long, duration: Long) {
        mediaAdapterCallback.setDuration(duration, position)
    }

    companion object {
        private val TAG = MediaAdapter::class.java.name
    }
}