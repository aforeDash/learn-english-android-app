package com.innovamates.learnenglish.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.data.DataConverter
import com.innovamates.learnenglish.data.models.SubCategory
import com.innovamates.learnenglish.views.adapters.DATA
import com.innovamates.learnenglish.views.adapters.SubCategoryListAdapter


class SubCategoryFragment : Fragment() {

    private lateinit var rvSubCategory: RecyclerView

    private var subCategoryList: List<SubCategory>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupData()
    }

    private fun setupData() {
        subCategoryList = arguments?.let { it ->
            it.getString(DATA)?.let {
                DataConverter.toSubCategoryList(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSubCategory = requireView().findViewById(R.id.rv_sub_category)

        subCategoryList?.let {
            rvSubCategory.apply {
                adapter = SubCategoryListAdapter(context, it)
                layoutManager = GridLayoutManager(context, 3)
            }
        }
    }
}