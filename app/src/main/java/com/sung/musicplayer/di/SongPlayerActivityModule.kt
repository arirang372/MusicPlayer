package com.sung.musicplayer.di

import com.sung.musicplayer.view.SongPlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SongPlayerActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeSongPlayerActivity(): SongPlayerActivity
}