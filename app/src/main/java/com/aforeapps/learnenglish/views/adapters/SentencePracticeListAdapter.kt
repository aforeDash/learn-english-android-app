package com.aforeapps.learnenglish.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.models.Sentence


class SentencePracticeListAdapter(
    private val context: Context,
    private val list: List<Sentence>,
    private val endTimeSec: Int,
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<SentencePracticeListAdapter.ViewHolder>() {

    private var currentPosition = 0
    private var lastPlayingPosition = 0

    class ViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvFirstSentence: TextView = itemView.findViewById(R.id.tv_first_sentence)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val btRecord: Button = itemView.findViewById(R.id.btn_record)
        private val checkBox1: CheckBox = itemView.findViewById(R.id.checkBox1)
        private val checkBox2: CheckBox = itemView.findViewById(R.id.checkBox2)
        private val tvCount: TextView = itemView.findViewById(R.id.tv_count)
        private val btPlay: ImageButton = itemView.findViewById(R.id.btn_play)

        fun bind(
            context: Context,
            list: List<Sentence>,
            position: Int,
            endTimeSec: Int,
            onItemClickListener: OnItemClickListener,
            sentencePracticeListAdapter: SentencePracticeListAdapter,
        ) {
            val sentence = list[position]
            tvFirstSentence.text = sentence.words.trim()

            if (sentence.isPlaying) {
                itemView.setBackgroundResource(R.drawable.border_shape_round_light_grey)
                tvFirstSentence.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                itemView.setBackgroundResource(R.drawable.border_shape_round_grey)
                tvFirstSentence.setTextColor(ContextCompat.getColor(context, R.color.dark_white))
            }

            if (sentence.audioTrack == 0) {
                checkBox1.isChecked = true
                checkBox2.isChecked = false
            } else {
                checkBox1.isChecked = false
                checkBox2.isChecked = true
            }

            val duration = if (position < list.size - 1) {
                val nextSentence = list[position + 1]
                val start = sentence.startTimeSec
                val end = nextSentence.startTimeSec
                (end - start) * 1000
            } else {
                val start = sentence.startTimeSec
                (endTimeSec - start) * 1000
            }

            btRecord.setOnClickListener {
                btRecord.text = context.getString(R.string.stop)
                btRecord.isEnabled = false

                onItemClickListener.onItemClick(position, duration, progressBar, btRecord, tvCount)
            }

            btPlay.setOnClickListener {
                onItemClickListener.onPlayClick(position)
            }

            checkBox1.setOnClickListener {
                sentence.audioTrack = 0
                sentencePracticeListAdapter.notifyItemChanged(position)
            }

            checkBox2.setOnClickListener {
                sentence.audioTrack = 1
                sentencePracticeListAdapter.notifyItemChanged(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_sentence_practice_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list, position, endTimeSec, onItemClickListener, this)
        currentPosition = holder.adapterPosition
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(sentence: Sentence, index: Int) {
        if (index < list.size) {
            (list as ArrayList)[index] = sentence
            notifyItemChanged(index)
        } else {
            (list as ArrayList).add(index, sentence)
            notifyItemInserted(index)
        }
    }

    fun activate(position: Int) {
        list[lastPlayingPosition].isPlaying = false
        notifyItemChanged(lastPlayingPosition)

        list[position].isPlaying = true
        notifyItemChanged(position)

        lastPlayingPosition = position
    }

    fun getCurrentPosition(): Int {
        return if (SentenceListAdapter.repeatEnabled) currentPosition
        else -1
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            duration: Long,
            progressBar: ProgressBar,
            btRecord: Button,
            tvCount: TextView,
        )

        fun onPlayClick(position: Int)
    }
}
