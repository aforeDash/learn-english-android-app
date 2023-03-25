package com.innovamates.learnenglish.views.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeThumbnailView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.utils.ThumbnailViewer
import com.innovamates.learnenglish.views.fragments.VideoListFragment

const val TAG = "VideoListAdapter"
const val DATA = "Data"

class VideoListAdapter(
    private val context: Context,
    private val list: List<VideoItem>,
    private val videoItemClickListener: VideoItemClickListener,
    private val fragment: VideoListFragment,
) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        private val thumbnailViewer: ThumbnailViewer,
        private val fragment: VideoListFragment,
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvFirstSentence: TextView = itemView.findViewById(R.id.tv_first_sentence)
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)

        fun bind(
            context: Context,
            videoItem: VideoItem,
            videoItemClickListener: VideoItemClickListener,
        ) {
            tvTitle.text = videoItem.title
            tvFirstSentence.text = videoItem.description

            thumbnailViewer.start(videoItem.youtube_id, ivThumbnail, fragment)

            itemView.setOnClickListener {
                videoItemClickListener.onVideoItemClicked(videoItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_video_list_item, parent, false)
        val thumbnailViewer = ThumbnailViewer(context)
        return ViewHolder(view, thumbnailViewer, fragment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position], videoItemClickListener)
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

    interface VideoItemClickListener {
        fun onVideoItemClicked(videoItem: VideoItem)
    }
}
