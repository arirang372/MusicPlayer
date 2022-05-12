package com.sung.musicplayer.view

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.sung.musicplayer.R
import java.io.File

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

//    @JvmStatic
//    @BindingAdapter(value = ["songs", "callback"])
//    fun setPlayList(view: RecyclerView, songs: List<Song>?, callback: PlayListCallback) {
//        songs?.let {
//            view.adapter = PlayListAdapter(callback).apply {
//                submitList(songs)
//            }
//        }
//    }
    /**
     *  since android does not provide the binding adapter for long click,
     *  we need to define one like below.
     */
    @JvmStatic
    @BindingAdapter("android:onLongClick")
    fun setOnLongClickListener(view: View, func: () -> Unit) {
        view.setOnLongClickListener {
            func.invoke()
            return@setOnLongClickListener true
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:src"])
    fun setImageResource(view: ImageView, source: String?) {
        source?.let {
            view.load(File(it)) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
                CachePolicy.ENABLED
            }
        }
    }
}