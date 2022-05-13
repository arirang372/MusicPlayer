package com.sung.musicplayer.model

import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong

data class SongPlayer(
    val isMusicPlaying: ObservableBoolean = ObservableBoolean(false),
    val toggleButtonImage: ObservableField<Drawable> = ObservableField(VectorDrawable()),
    val title: ObservableField<String> = ObservableField(""),
    val artist: ObservableField<String> = ObservableField(""),
    val length: ObservableLong = ObservableLong(0L),
    val passedTime: ObservableField<String> = ObservableField(""),
    val passedTimeProgress: ObservableInt = ObservableInt(0),
    val totalTime: ObservableField<String> = ObservableField(""),
    val songImage: ObservableField<String> = ObservableField(""),
    val songDuration : ObservableInt = ObservableInt(0)
)
