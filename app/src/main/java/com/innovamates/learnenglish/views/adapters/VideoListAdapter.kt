package com.innovamates.learnenglish.views.adapters

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeThumbnailView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.data.database.typeconverter.DataConverter
import com.innovamates.learnenglish.utils.getNavigationAnimation
import com.innovamates.learnenglish.utils.setVideoThumbnail

const val TAG = "VideoListAdapter"
const val DATA = "Data"

class VideoListAdapter(
    private val context: Context,
    private val list: List<VideoItem>,
) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvFirstSentence: TextView = itemView.findViewById(R.id.tv_first_sentence)
        private val ivThumbnail: YouTubeThumbnailView = itemView.findViewById(R.id.iv_thumbnail)

        fun bind(
            context: Context,
            videoItem: VideoItem,
        ) {
            tvTitle.text = videoItem.title
            tvFirstSentence.text = videoItem.sentences[0].sentence

            Handler(Looper.getMainLooper()).post {
                Glide.with(context)
                    .load("https://img.youtube.com/vi/${videoItem.videoId}/0.jpg")
                    .into(ivThumbnail)
            }

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(DATA, DataConverter.fromVideoItemList(arrayListOf(videoItem)))

                val navHostFragment =
                    (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                val navController = navHostFragment.navController

                navController.navigate(
                    R.id.navigation_player_fragment,
                    bundle,
                    navController.getNavigationAnimation()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_video_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(videoItem: VideoItem, index: Int) {
        if (index < list.size) {
            (list as ArrayList)[index] = videoItem
            notifyItemChanged(index)
        } else {
            (list as ArrayList).add(index, videoItem)
            notifyItemInserted(index)
        }
    }
}
