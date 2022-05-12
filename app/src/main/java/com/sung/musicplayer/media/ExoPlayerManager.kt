//package com.sung.musicplayer.media
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.media.AudioManager
//import android.net.wifi.WifiManager
//import android.os.Handler
//import android.os.Looper
//import android.os.Message
//import com.google.android.exoplayer2.*
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
//import com.sung.musicplayer.model.ASong
//import com.sung.musicplayer.service.SongPlayerService
//
///**
// * This class is responsible for managing the player(actions, state, ...) using google ExoPlayer
// *
// * @author John Sung
// */
//class ExoPlayerManager(val context: Context) : OnExoPlayerManagerCallback {
//
//    private var mWifiLock: WifiManager.WifiLock? = null
//    private var mAudioManager: AudioManager? = null
//    private val mEventListener = ExoPlayerEventListener()
//    private val mAudioNoisyIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
//    private var mExoSongStateCallback: OnExoPlayerManagerCallback.OnSongStateCallback? = null
//    private var mAudioNoisyReceiverRegistered: Boolean = false
//    private var mCurrentSong: ASong? = null
//    private var mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
//    private var mExoPlayer: SimpleExoPlayer? = null
//    private var mPlayOnFocusGain: Boolean = false
//    private val bandwidthMeter = DefaultBandwidthMeter()
//
//    private val mAudioNoisyReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
//                if (mPlayOnFocusGain || mExoPlayer != null && mExoPlayer?.playWhenReady == true) {
//                    val intent = Intent(context, SongPlayerService::class.java).apply {
//                        action = SongPlayerService.ACTION_CMD
//                        putExtra(SongPlayerService.CMD_NAME, SongPlayerService.CMD_PAUSE)
//                    }
//                    context.applicationContext.startService(intent)
//                }
//            }
//        }
//    }
//
//    private val mUpdateProgressHandler = object : Handler(Looper.getMainLooper()){
//        override fun handleMessage(msg: Message) {
//            val duration = mExoPlayer?.duration ?: 0
//            val position = mExoPlayer?.currentPosition ?: 0
//
//        }
//    }
//
//
//    override fun getCurrentStreamPosition(): Long {
//        return 0
//    }
//
//    override fun stop() {
//    }
//
//    override fun play(aSong: ASong) {
//    }
//
//    override fun pause() {
//    }
//
//    override fun seekTo(position: Long) {
//    }
//
//    override fun setCallback(callback: OnExoPlayerManagerCallback.OnSongStateCallback) {
//    }
//
//
//    /**
//     * Releases resources used by the service for playback, which is mostly just the WiFi lock for
//     * local playback. If requested, the ExoPlayer instance is also released.
//     *
//     * @param releasePlayer Indicates whether the player should also be released
//     */
//    private fun releaseResources(releasePlayer: Boolean) {
//        // Stops and releases player (if requested and available).
//        if (releasePlayer) {
//            mUpdateProgressHandler.removeMessages(0)
//            mExoPlayer?.release()
//            mExoPlayer?.removeListener(mEventListener)
//            mExoPlayer = null
//            mExoPlayerIsStopped = true
//            mPlayOnFocusGain = false
//        }
//
//        if (mWifiLock?.isHeld == true) {
//            mWifiLock?.release()
//        }
//    }
//
//    private fun registerAudioNoisyReceiver() {
//        if (!mAudioNoisyReceiverRegistered) {
//            context.applicationContext.registerReceiver(mAudioNoisyReceiver,
//                mAudioNoisyIntentFilter)
//            mAudioNoisyReceiverRegistered = true
//        }
//    }
//
//    private fun unregisterAudioNoisyReceiver(){
//        if(mAudioNoisyReceiverRegistered){
//            context.applicationContext.unregisterReceiver(mAudioNoisyReceiver)
//            mAudioNoisyReceiverRegistered = false
//        }
//    }
//
//    private inner class ExoPlayerEventListener : Player.EventListener {
//
//        override fun onLoadingChanged(isLoading: Boolean) {
//            // Nothing to do.
//        }
//
//        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//            when (playbackState) {
//                Player.STATE_IDLE, Player.STATE_BUFFERING, Player.STATE_READY -> {
//                    setCurrentSongState()
//                    mUpdateProgressHandler.sendEmptyMessage(0)
//                }
//                Player.STATE_ENDED -> {
//                    // The media player finished playing the current song.
//                    mUpdateProgressHandler.removeMessages(0)
//                    mExoSongStateCallback?.onCompletion()
//                }
//            }
//        }
//
//        override fun onPlayerError(error: ExoPlaybackException) {
//            val what: String = when (error.type) {
//                ExoPlaybackException.TYPE_SOURCE -> error.sourceException.message ?: ""
//                ExoPlaybackException.TYPE_RENDERER -> error.rendererException.message ?: ""
//                ExoPlaybackException.TYPE_UNEXPECTED -> error.unexpectedException.message ?: ""
//                else -> "onPlayerError: $error"
//            }
//        }
//
//        override fun onPositionDiscontinuity(reason: Int) {
//            // Nothing to do.
//        }
//
//        override fun onSeekProcessed() {
//            // Nothing to do.
//        }
//
//        override fun onRepeatModeChanged(repeatMode: Int) {
//            // Nothing to do.
//        }
//
//        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
//            // Nothing to do.
//        }
//    }
//
//    companion object {
//
//        private val TAG = ExoPlayerManager::class.java.name
//        const val UPDATE_PROGRESS_DELAY = 500L
//
//        // The volume we set the media player to when we lose audio focus, but are
//        // allowed to reduce the volume instead of stopping playback.
//        private const val VOLUME_DUCK = 0.2f
//
//        // The volume we set the media player when we have audio focus.
//        private const val VOLUME_NORMAL = 1.0f
//
//        // we don't have audio focus, and can't duck (play at a low volume)
//        private const val AUDIO_NO_FOCUS_NO_DUCK = 0
//
//        // we don't have focus, but can duck (play at a low volume)
//        private const val AUDIO_NO_FOCUS_CAN_DUCK = 1
//
//        // we have full audio focus
//        private const val AUDIO_FOCUSED = 2
//    }
//}