package com.sung.musicplayer.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Song(
    @PrimaryKey
    var id: Int,
    var title: String? = "",
    var path: String,
    var artist: String? = "",
    var clipArt: String? = "",
    var duration: String? = "",
    var songType: Int = 0,
) : Parcelable