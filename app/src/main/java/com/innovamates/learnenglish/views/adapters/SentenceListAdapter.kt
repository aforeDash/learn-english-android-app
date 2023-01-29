package com.innovamates.learnenglish.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.data.models.Sentence


class SentenceListAdapter(
    private val context: Context,
    private val list: List<Sentence>,
) : RecyclerView.Adapter<SentenceListAdapter.ViewHolder>() {

    init {
        repeatEnabled = false
    }

    private var currentPosition = 0

    class ViewHolder(itemView: View, sentenceListAdapter: SentenceListAdapter) :
        RecyclerView.ViewHolder(itemView) {

        private val tvFirstSentence: TextView = itemView.findViewById(R.id.tv_first_sentence)
        private val tvPage: TextView = itemView.findViewById(R.id.tv_page)
        private val ibRepeat: ImageButton = itemView.findViewById(R.id.ib_repeat)

        init {
            ibRepeat.setOnClickListener {
                repeatEnabled = !repeatEnabled
                sentenceListAdapter.notifyDataSetChanged()
            }
        }

        fun bind(
            context: Context,
            sentence: Sentence,
            current: Int,
            total: Int,
        ) {
            tvFirstSentence.text = sentence.sentence

            val pages = context.getString(R.string.pages)
                .replace("#", (current + 1).toString())
                .replace("$", total.toString())

            tvPage.text = pages

            setupRepeat(ibRepeat)

        }

        private fun setupRepeat(ibRepeat: ImageButton) {
            if (repeatEnabled) {
                ibRepeat.setBackgroundResource(R.drawable.border_shape_round_black)
            } else {
                ibRepeat.setBackgroundResource(R.drawable.border_shape_round_white)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_sentence_list_item, parent, false)
        return ViewHolder(view, this)
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

    fun getCurrentPosition(): Int {
        return if (repeatEnabled)
            currentPosition
        else
            -1
    }

    companion object {
        var repeatEnabled = false
    }
}
