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

class SongPlayerActivity : DaggerAppCompatActivity(), OnPlayerServiceCallback {
    private lateinit var binding: ActivitySongPlayerBinding
    private var mService: SongPlayerService? = null
    private var bound = false
    private var msg = 0

    private var song: Song? = null
    private var songList: MutableList<Song>? = null

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val songPlayerViewModel: SongPlayerViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var songId = 0
        intent?.extras?.apply {
            if (containsKey(SONG_ID)) {
                songId = getInt(SONG_ID)
            }
        }

        songPlayerViewModel.songList.observe(this) {

        }

        songPlayerViewModel.getSongBy(songId).observe(this) {
            songPlayerViewModel.setUpModel(resources::getDrawable, it)
        }

        binding = ActivitySongPlayerBinding.inflate(layoutInflater).apply {
            this.model = songPlayerViewModel.songPlayer
        }
        setContentView(binding.root)
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


    override fun updateSongData(song: Song) {
        TODO("Not yet implemented")
    }

    override fun updateSongProgress(duration: Long, position: Long) {
        TODO("Not yet implemented")
    }

    override fun setBufferingData(isBuffering: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setVisibilityData(isVisibility: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setPlayStatus(isPlay: Boolean) {
        TODO("Not yet implemented")
    }

    override fun stopService() {
        TODO("Not yet implemented")
    }

    companion object {

        private val TAG = SongPlayerActivity::class.java.name
        const val SONG_LIST_KEY = "SONG_LIST_KEY"
        const val SONG_ID = "SONG_ID"
        private const val ACTION_PLAY_SONG_IN_LIST = 1
        private const val ACTION_PAUSE = 2
        private const val ACTION_STOP = 3
    }
}