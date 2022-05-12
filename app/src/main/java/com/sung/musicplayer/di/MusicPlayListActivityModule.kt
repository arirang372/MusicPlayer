package com.sung.musicplayer.di

import com.sung.musicplayer.view.MusicPlayListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MusicPlayListActivityModule {

    @ContributesAndroidInjector(modules = [PlayListFragmentBuilderModule::class])
    abstract fun contributeMusicPlayListActivity(): MusicPlayListActivity
}