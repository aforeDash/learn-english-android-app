package com.innovamates.learnenglish.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeThumbnailView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.data.database.typeconverter.DataConverter
import com.innovamates.learnenglish.data.models.Category
import com.innovamates.learnenglish.utils.getNavigationAnimation
import com.innovamates.learnenglish.utils.setVideoThumbnail

class CategoryListAdapter(
    private val context: Context,
    private val list: List<Category>,
) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        private val rvSubCategory: RecyclerView = itemView.findViewById(R.id.rv_sub_category)
        private val horizontalLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        private val subCategoryListAdapter = SubCategoryListAdapter(itemView.context, arrayListOf())

        init {
            rvSubCategory.apply {
                adapter = subCategoryListAdapter
                layoutManager = horizontalLayoutManager
            }
        }

        fun bind(
            context: Context,
            category: Category,
        ) {
            tvCategoryName.text = category.name
            subCategoryListAdapter.addItem(category.sub_categories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_category_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(category: Category, index: Int) {
        if (index < list.size) {
            (list as ArrayList)[index] = category
            notifyItemChanged(index)
        } else {
            (list as ArrayList).add(index, category)
            notifyItemInserted(index)
        }
    }
}
