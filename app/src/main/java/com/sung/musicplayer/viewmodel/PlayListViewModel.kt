package com.sung.musicplayer.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sung.musicplayer.data.MusicPlayerRepository
import com.sung.musicplayer.model.Song
import com.sung.musicplayer.view.MusicPlayListActivity.Companion.AUDIO_TYPE
import com.sung.musicplayer.view.MusicPlayListActivity.Companion.PICK_AUDIO_KEY
import com.sung.musicplayer.view.MusicPlayListActivity.Companion.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class PlayListViewModel @Inject constructor(private val repository: MusicPlayerRepository) :
    ViewModel() {

    val songs : LiveData<MutableList<Song>> = repository.getSongs()

    fun onFloatingButtonClick(activity: Activity) {
        if (isReadStoragePermissionGranted(activity)) {
            openMusicList(activity)
        } else {
            activity.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE)
        }
    }

    fun addSong(context: Context, uri: Uri) {
        viewModelScope.launch {
            val cursor = context.contentResolver?.query(
                uri,
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION
                ), null, null, null
            )
            while (cursor?.moveToNext() == true) {
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val albumId =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val duration =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                val cursorAlbums = context.contentResolver?.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART),
                    MediaStore.Audio.Albums._ID + "=?",
                    arrayOf<String>(albumId),
                    null
                )
                var albumArt: String? = null
                if (cursorAlbums?.moveToFirst() == true) {
                    albumArt =
                        cursorAlbums.getString(cursorAlbums.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                }

                val song = Song(id.toInt(),
                    title.toString(),
                    path.toString(),
                    artist,
                    albumArt,
                    duration,
                    AUDIO_TYPE
                )
                repository.saveSong(song)
            }
            cursor?.close()
        }
    }

    fun removeSong(song : Song){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(song)
        }
    }

    fun openMusicList(activity: Activity) {
        activity.startActivityForResult(Intent(Intent.ACTION_PICK,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), PICK_AUDIO_KEY)
    }

    fun logError(activity: Activity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun isReadStoragePermissionGranted(activity: Activity) =
        ContextCompat.checkSelfPermission(activity,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}