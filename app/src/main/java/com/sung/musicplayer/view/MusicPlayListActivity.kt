package com.sung.musicplayer.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sung.musicplayer.R
import com.sung.musicplayer.databinding.ActivityMusicPlayListBinding
import com.sung.musicplayer.viewmodel.PlayListViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MusicPlayListActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val playListViewModel: PlayListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_AUDIO_KEY) {
            data?.data?.let {
                playListViewModel.addSong(this, it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                playListViewModel.openMusicList(this)
            } else {
                playListViewModel.logError(this, getString(R.string.you_denied_permission))
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE = 7031
        const val PICK_AUDIO_KEY = 2017
        const val AUDIO_TYPE = 3
    }
}