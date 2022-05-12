package com.sung.musicplayer.data

import androidx.lifecycle.LiveData
import com.sung.musicplayer.model.Song
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MusicPlayerRepository @Inject constructor(private val songDao: SongDao) {

    suspend fun delete(song: Song) {
        songDao.delete(song)
    }

    fun getSongs(): LiveData<MutableList<Song>> = songDao.loadAll()

    suspend fun saveSong(song: Song): Long {
        return songDao.insert(song)
    }

    suspend fun getSongById(songId : Int) : Song = songDao.loadOneBySongId(songId)

}