package com.sung.musicplayer.di

import com.sung.musicplayer.view.PlayListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PlayListFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributePlayListFragment(): PlayListFragment
}