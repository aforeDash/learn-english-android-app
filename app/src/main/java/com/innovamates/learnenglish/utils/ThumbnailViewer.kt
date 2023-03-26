package com.innovamates.learnenglish.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.innovamates.learnenglish.views.fragments.VideoListFragment


class ThumbnailViewer(
    private val context: Context,
) {

    private fun loadThumbnail(imageView: ImageView, youtubeId: String, i: Int) {
        Log.d("simul", "$i")
        Glide.with(context)
            .load("https://img.youtube.com/vi/$youtubeId/${i}.jpg")
            .into(imageView)
    }

    fun start(youtubeId: String, imageView: ImageView, fragment: VideoListFragment) {
        val handler = Handler(Looper.getMainLooper())
        val runnable: Runnable = object : Runnable {
            var index = 0
            override fun run() {
                loadThumbnail(imageView, youtubeId, index)
                index = (index + 1) % 4
                if (fragment.isVisible)
                    handler.postDelayed(this, 2000)
                else {
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.postDelayed(runnable, 0)
    }


}