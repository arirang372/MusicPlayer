package com.sung.musicplayer.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sung.musicplayer.R
import com.sung.musicplayer.data.MusicPlayerRepository
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.model.SongPlayer
import com.sung.musicplayer.utils.formatTimeInMillisToString
import javax.inject.Inject

class SongPlayerViewModel @Inject constructor(private val repository: MusicPlayerRepository) :
    ViewModel() {

    private var songLiveData: LiveData<Song>? = null
    val songList : LiveData<MutableList<Song>> = repository.getSongs()
    var songPlayer = SongPlayer()

    fun getSongBy(id: Int): LiveData<Song> {
        return songLiveData ?: liveData {
            emit(repository.getSongById(id))
        }.also {
            songLiveData = it
        }
    }

    fun setUpModel(getDrawable: (Int) -> Drawable, song: Song) {
        with(songPlayer) {
            isMusicPlaying.set(true)
            songImage.set(song.clipArt)
            toggleButtonImage.set(if (isMusicPlaying.get()) getDrawable(R.drawable.ic_pause_vector) else getDrawable(
                R.drawable.ic_play_vector))
            title.set(song.title)
            artist.set(song.artist)
            totalTime.set(formatTimeInMillisToString(song.duration?.toLong() ?: 0L))
        }
    }

}