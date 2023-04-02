package com.aforeapps.learnenglish.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.DataConverter
import com.aforeapps.learnenglish.data.models.SubCategory
import com.aforeapps.learnenglish.utils.getNavigationAnimation
import com.bumptech.glide.Glide

class SubCategoryListAdapter(
    private val context: Context,
    private val list: List<SubCategory>,
) :
    RecyclerView.Adapter<SubCategoryListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvSubCategoryName: TextView = itemView.findViewById(R.id.tv_sub_category_name)
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)

        private var subCategory: SubCategory? = null

        init {
            itemView.setOnClickListener {
                subCategory?.let {
                    val bundle = Bundle()
                    bundle.putString(
                        DATA,
                        DataConverter.fromSubCategory(it)
                    )

                    val navHostFragment =
                        (itemView.context as AppCompatActivity).supportFragmentManager.findFragmentById(
                            R.id.nav_host_fragment_activity_main
                        ) as NavHostFragment
                    val navController = navHostFragment.navController

                    navController.navigate(
                        R.id.navigation_video_list_fragment,
                        bundle,
                        navController.getNavigationAnimation()
                    )
                }
            }
        }

        fun bind(
            context: Context,
            subCategory: SubCategory,
        ) {
            this.subCategory = subCategory

            tvSubCategoryName.text = subCategory.name

            Glide.with(context)
                .load(subCategory.thumbnail_url)
                .placeholder(R.drawable.image)
                .into(ivImage)
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
