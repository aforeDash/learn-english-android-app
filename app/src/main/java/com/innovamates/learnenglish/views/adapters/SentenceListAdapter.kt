package com.innovamates.learnenglish.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.models.videoitem.Sentence


class SentenceListAdapter(
    private val context: Context,
    private val list: List<Sentence>,
) :
    RecyclerView.Adapter<SentenceListAdapter.ViewHolder>() {

    private var currentPosition = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvFirstSentence: TextView = itemView.findViewById(R.id.tv_first_sentence)
        private val tvPage: TextView = itemView.findViewById(R.id.tv_page)

        fun bind(
            context: Context,
            sentence: Sentence,
            current: Int,
            total: Int,
        ) {
            tvFirstSentence.text = sentence.sentence
            tvPage.text = "$current/$total"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_sentence_list_item, parent, false)
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
        } else {
            (list as ArrayList).add(index, sentence)

        }
        notifyItemInserted(index)
    }

    fun getCurrentPosition(): Int {
//       return currentPosition
        return -1
    }
}
