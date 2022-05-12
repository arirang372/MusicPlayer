package com.sung.musicplayer.di

import android.app.Application
import androidx.room.Room
import com.sung.musicplayer.data.MusicPlayerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): MusicPlayerDatabase {
        return Room.databaseBuilder(app,
            MusicPlayerDatabase::class.java,
            MusicPlayerDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSongDao(db: MusicPlayerDatabase) = db.songDao




}