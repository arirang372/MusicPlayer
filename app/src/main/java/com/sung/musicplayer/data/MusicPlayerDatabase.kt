package com.sung.musicplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sung.musicplayer.model.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class MusicPlayerDatabase : RoomDatabase() {

    abstract val songDao: SongDao

    companion object {
        const val DB_NAME = "MusicPlayer.db"
    }
}