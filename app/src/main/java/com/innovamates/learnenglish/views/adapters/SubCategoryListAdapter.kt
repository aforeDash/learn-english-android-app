package com.innovamates.learnenglish.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.data.models.SubCategory

class SubCategoryListAdapter(
    private val context: Context,
    private val list: List<SubCategory>,
) :
    RecyclerView.Adapter<SubCategoryListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvSubCategoryName: TextView = itemView.findViewById(R.id.tv_sub_category_name)

        fun bind(
            context: Context,
            subCategory: SubCategory,
        ) {
            tvSubCategoryName.text = subCategory.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_sub_category_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(subCategories: List<SubCategory>) {
        subCategories.forEachIndexed { index, subCategory ->
            if (index < list.size) {
                (list as ArrayList)[index] = subCategory
                notifyItemChanged(index)
            } else {
                (list as ArrayList).add(index, subCategory)
                notifyItemInserted(index)
            }
        }
    }
}
