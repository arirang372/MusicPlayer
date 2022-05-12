package com.sung.musicplayer.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sung.musicplayer.model.Song


@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song): Long

    /**
     *  Since loadAll() method provides data with LiveData
     *  we don't need to call it with suspend
     */
    @Query("SELECT * FROM Song")
    fun loadAll(): LiveData<MutableList<Song>>

    @Delete
    suspend fun delete(song: Song)

    @Query("DELETE FROM Song")
    suspend fun deleteAll()

    @Query("SELECT * FROM Song where id = :id")
    suspend fun loadOneBySongId(id: Int): Song

    @Query("SELECT * FROM Song where title = :title")
    suspend fun loadOneBySongTitle(title: String): Song?

    @Update
    suspend fun update(song: Song)
}