package com.sung.musicplayer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sung.musicplayer.viewmodel.PlayListViewModel
import com.sung.musicplayer.viewmodel.SongPlayerViewModel
import com.sung.musicplayer.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass


@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PlayListViewModel::class)
    abstract fun bindPlayListViewModel(playListViewModel: PlayListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SongPlayerViewModel::class)
    abstract fun bindSongPlayerViewModel(songPlayerViewModel: SongPlayerViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factor: ViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)