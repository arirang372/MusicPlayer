package com.sung.musicplayer.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sung.musicplayer.databinding.ActivitySongPlayerBinding
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.service.OnPlayerServiceCallback
import com.sung.musicplayer.service.SongPlayerService
import com.sung.musicplayer.viewmodel.SongPlayerViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class SongPlayerActivity : DaggerAppCompatActivity(), OnPlayerServiceCallback, SongPlayerCallback {
    private lateinit var binding: ActivitySongPlayerBinding
    private var bound = false
    private var mService: SongPlayerService? = null
    private var msg = 0
    private var song: Song? = null
    private var songList: MutableList<Song>? = null
    private val songPlayerViewModel: SongPlayerViewModel by viewModels {
        viewModelFactory
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ACTION_PLAY_SONG_IN_LIST -> mService?.play(songList, song)
                ACTION_PAUSE -> mService?.pause()
                ACTION_STOP -> {
                    mService?.stop()
                    songPlayerViewModel.stop()
                }
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as SongPlayerService.LocalBinder
            mService = binder.service
            bound = true
            mService?.subscribeToSongPlayerUpdates()
            handler.sendEmptyMessage(msg)
            mService?.addListener(this@SongPlayerActivity)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
            mService?.removeListener()
            mService = null
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var songId = 0
        intent?.extras?.apply {
            if (containsKey(SONG_ID)) {
                songId = getInt(SONG_ID)
            }
        }

        songPlayerViewModel.songList.observe(this) { songList ->
            songPlayerViewModel.getSongBy(songId).observe(this) { song ->
                songPlayerViewModel.setUpModel(song)
                play(songList, song)
            }
        }

        binding = ActivitySongPlayerBinding.inflate(layoutInflater).apply {
            this.model = songPlayerViewModel.songPlayer
            this.callback = this@SongPlayerActivity
        }
        setContentView(binding.root)
    }


    private fun bindPlayerService() {
        if (!bound)
            bindService(
                Intent(this, SongPlayerService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
    }

    private fun play(songList: MutableList<Song>?, song: Song) {
        msg = ACTION_PLAY_SONG_IN_LIST
        this.song = song
        this.songList = songList
        songPlayerViewModel.setIsMusicPlaying(true)
        if (mService == null)
            bindPlayerService()
        else
            handler.sendEmptyMessage(msg)
    }

    private fun pause() {
        msg = ACTION_PAUSE
        songPlayerViewModel.setIsMusicPlaying(false)
        if (mService == null)
            bindPlayerService()
        else
            handler.sendEmptyMessage(msg)
    }

    override fun updateSongData(song: Song) {
        songPlayerViewModel.setUpModel(song)
        songPlayerViewModel.setIsMusicPlaying(true)
    }

    override fun updateSongProgress(duration: Long, position: Long) {
        songPlayerViewModel.updateSongProgress(position, duration)
    }

    override fun stopService() {
        unbindService()
        mService = null
    }

    private fun unbindService() {
        if (bound) {
            unbindService(serviceConnection)
            bound = false
        }
    }

    override fun onNextClick() {
        mService?.skipToNext()
    }

    override fun onPrevClick() {
        mService?.skipToPrevious()
    }

    override fun onToggleClick() {
        songPlayerViewModel.onToggleClick(this::pause, this::play)
    }

    override fun onProgressChanged(progress: Int, fromUser: Boolean) {
        songPlayerViewModel.onProgressChanged(progress, fromUser)
        if (fromUser)
            mService?.seekTo(progress.toLong())

    }

    companion object {
        private const val ACTION_PLAY_SONG_IN_LIST = 1
        private const val ACTION_PAUSE = 2
        private const val ACTION_STOP = 3
        const val SONG_ID = "SONG_ID"
        private val TAG = SongPlayerActivity::class.java.name
    }
}

