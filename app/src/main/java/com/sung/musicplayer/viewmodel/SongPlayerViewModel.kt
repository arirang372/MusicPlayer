package com.sung.musicplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sung.musicplayer.data.MusicPlayerRepository
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.model.SongPlayer
import com.sung.musicplayer.utils.formatTimeInMillisToString
import javax.inject.Inject

class SongPlayerViewModel @Inject constructor(private val repository: MusicPlayerRepository) :
    ViewModel() {

    val songList: LiveData<MutableList<Song>> = repository.getSongs()
    private var songLiveData: LiveData<Song>? = null
    var songPlayer = SongPlayer()

    fun getSongBy(id: Int): LiveData<Song> {
        return songLiveData ?: liveData {
            emit(repository.getSongById(id))
        }.also {
            songLiveData = it
        }
    }

    fun onProgressChanged(progress: Int, fromUser: Boolean) {
        if (fromUser)
            seekTo(progress.toLong())
    }

    fun onToggleClick(
        pause: () -> Unit,
        play: (songList: MutableList<Song>?, song: Song) -> Unit,
    ) {
        if (songPlayer.isMusicPlaying.get())
            pause.invoke()
        else
            songLiveData?.value?.let {
                play.invoke(songList.value, it)
            }
    }

    private fun seekTo(position: Long) {
        songPlayer.passedTime.set(formatTimeInMillisToString(position))
        songPlayer.passedTimeProgress.set(position.toInt())
    }

    fun setIsMusicPlaying(isMusicPlaying: Boolean) {
        songPlayer.isMusicPlaying.set(isMusicPlaying)
    }

    fun setUpModel(song: Song?) {
        with(songPlayer) {
            songImage.set(song?.clipArt)
            title.set(song?.title)
            artist.set(song?.artist)
            totalTime.set(formatTimeInMillisToString(song?.duration?.toLong() ?: 0L))
        }
    }

    fun stop(){
        setIsMusicPlaying(false)
        seekTo(songPlayer.passedTimeProgress.get().toLong())
    }

    fun updateSongProgress(currentPosition: Long, duration: Long) {
        if (currentPosition > duration) return
        songPlayer.passedTime.set(formatTimeInMillisToString(currentPosition))
        songPlayer.passedTimeProgress.set(currentPosition.toInt())
        if (songPlayer.songDuration.get() == 0)
            songPlayer.songDuration.set(duration.toInt())
        //When Song ends, we want to change the pause button to the play button
        if (songPlayer.passedTime.get() == songPlayer.totalTime.get()) {
            setIsMusicPlaying(false)
        }
    }
}