package com.innovamates.learnenglish.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.youtube.player.Constants
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.views.adapters.TAG

fun setVideoThumbnail(
    videoId: String,
    ivThumbnail: YouTubeThumbnailView,
) {
    ivThumbnail.initialize(Constants.API_KEY,
        object : YouTubeThumbnailView.OnInitializedListener {
            override fun onInitializationSuccess(
                youTubeThumbnailView: YouTubeThumbnailView,
                youTubeThumbnailLoader: YouTubeThumbnailLoader,
            ) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(videoId)
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                    YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                    override fun onThumbnailLoaded(
                        youTubeThumbnailView: YouTubeThumbnailView,
                        s: String,
                    ) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release()
                    }

                    override fun onThumbnailError(
                        youTubeThumbnailView: YouTubeThumbnailView,
                        errorReason: YouTubeThumbnailLoader.ErrorReason,
                    ) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error")
                    }
                })
            }

            override fun onInitializationFailure(
                youTubeThumbnailView: YouTubeThumbnailView,
                youTubeInitializationResult: YouTubeInitializationResult,
            ) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure")
            }
        })
}


fun NavController.getNavigationAnimation(): NavOptions {
    val navBuilder = NavOptions.Builder()
    navBuilder.setEnterAnim(R.anim.right_in).setExitAnim(R.anim.left_out)
        .setPopEnterAnim(R.anim.left_in).setPopExitAnim(R.anim.right_out)
    return navBuilder.build()
}

fun View.removeParent() {
    if (this.parent != null) {
        (this.parent as ViewGroup).removeView(
            this)
    }
}