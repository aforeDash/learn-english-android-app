package com.aforeapps.learnenglish.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.models.Sentence

class SentenceVerticalListAdapter(
    private val context: Context,
    private val list: List<Sentence>,
) : RecyclerView.Adapter<SentenceVerticalListAdapter.ViewHolder>() {

    private var currentPosition = 0
    private var lastPlayingPosition = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvFirstSentence: TextView = itemView.findViewById(R.id.tv_first_sentence)

        fun bind(
            context: Context,
            sentence: Sentence,
            current: Int,
            total: Int,
        ) {
            tvFirstSentence.text = sentence.words.trim()
            if (sentence.isPlaying) {
                itemView.setBackgroundResource(R.drawable.border_shape_round_light_grey)
                tvFirstSentence.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                itemView.setBackgroundResource(R.drawable.border_shape_round_grey)
                tvFirstSentence.setTextColor(ContextCompat.getColor(context, R.color.dark_white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_sentence_vertical_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position], position, list.size)
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
        return if (SentenceListAdapter.repeatEnabled)
            currentPosition
        else
            -1
    }
}
