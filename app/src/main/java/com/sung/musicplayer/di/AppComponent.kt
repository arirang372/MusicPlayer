package com.sung.musicplayer.di

import android.app.Application
import com.sung.musicplayer.MusicPlayerApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, MusicPlayListActivityModule::class,
    SongPlayerActivityModule::class])
interface AppComponent : AndroidInjector<MusicPlayerApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: MusicPlayerApp)
}
