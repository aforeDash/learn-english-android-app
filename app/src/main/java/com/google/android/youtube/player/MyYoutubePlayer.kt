package com.google.android.youtube.player

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.innovamates.learnenglish.R

class MyYoutubePlayer(val context: Context) {

    private lateinit var _youtubePlayer: YouTubePlayer
    val youtubePlayer: YouTubePlayer
        get() = _youtubePlayer

    private var _videoView =
        LayoutInflater.from(context).inflate(R.layout.youtube_fragment_layout, null)
    val videoView: View
        get() = _videoView

    private var _youtubePlayerFragment = (context as AppCompatActivity).supportFragmentManager
        .findFragmentById(R.id.youtube_fragment) as YoutubePlayerSupportFragmentX
    val youtubePlayerFragment: YoutubePlayerSupportFragmentX
        get() = _youtubePlayerFragment

    private var _isPlayerReady = false
    private val isPlayerReady: Boolean
        get() = _isPlayerReady

    private var onPlayerReadyCallback: OnPlayerReadyCallback? = null

    init {
        _youtubePlayerFragment.initialize(Constants.API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    youTubePlayer: YouTubePlayer?,
                    p2: Boolean,
                ) {
                    if (youTubePlayer != null) {
                        this@MyYoutubePlayer._youtubePlayer = youTubePlayer
                        _youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)

                        _isPlayerReady = true
                        onPlayerReadyCallback?.onPlayerReady(_youtubePlayer)
                    }
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?,
                ) {
                    Toast.makeText(context,
                        "Video player Failed",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun setOnPlayerReadyCallback(onPlayerReadyCallback: OnPlayerReadyCallback) {
        this.onPlayerReadyCallback = onPlayerReadyCallback
        if (isPlayerReady) {
            onPlayerReadyCallback.onPlayerReady(youtubePlayer)
        }
    }

    interface OnPlayerReadyCallback {
        fun onPlayerReady(youTubePlayer: YouTubePlayer)
    }
}

