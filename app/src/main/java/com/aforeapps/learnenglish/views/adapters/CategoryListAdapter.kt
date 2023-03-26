package com.aforeapps.learnenglish.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.DataConverter
import com.aforeapps.learnenglish.data.models.Category
import com.aforeapps.learnenglish.utils.getNavigationAnimation

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
                itemAnimator = null
            }
        }

        fun bind(
            context: Context,
            category: Category,
        ) {
            tvCategoryName.text = category.name
            subCategoryListAdapter.addItem(category.sub_categories)

            tvCategoryName.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(
                    DATA,
                    DataConverter.fromSubCategoryList(category.sub_categories)
                )

                val navHostFragment =
                    (itemView.context as AppCompatActivity).supportFragmentManager.findFragmentById(
                        R.id.nav_host_fragment_activity_main
                    ) as NavHostFragment
                val navController = navHostFragment.navController

                navController.navigate(
                    R.id.navigation_sub_category_fragment,
                    bundle,
                    navController.getNavigationAnimation()
                )
            }
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
