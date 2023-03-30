package com.aforeapps.learnenglish.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.aforeapps.learnenglish.databinding.FragmentHomeBinding
import com.aforeapps.learnenglish.utils.OnSnapPositionChangeListener
import com.aforeapps.learnenglish.utils.SnapHelper
import com.aforeapps.learnenglish.utils.SnapOnScrollListener
import com.aforeapps.learnenglish.utils.attachSnapHelperWithListener
import com.aforeapps.learnenglish.viewmodels.HomeViewModel
import com.aforeapps.learnenglish.views.adapters.CategoryListAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            HomeViewModel.Factory(activity.application)
        )[HomeViewModel::class.java]
    }

    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            categoryListAdapter = CategoryListAdapter(it, arrayListOf())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvVideoList?.apply {
            adapter = categoryListAdapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            itemAnimator = null
        }

        binding?.rvVideoList?.attachSnapHelperWithListener(SnapHelper().snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {}
            })

        binding?.btReload?.setOnClickListener {
            observerData()
        }

        if (categoryListAdapter.itemCount == 0) {
            observerData()
        }
    }

    private fun observerData() {
        binding?.progressMain?.visibility = View.VISIBLE
        homeViewModel.getCategories().observe(viewLifecycleOwner) {
            it?.categories?.forEachIndexed { index, category ->
                categoryListAdapter.addItem(category, index)
            } ?: run {
                Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show()
            }
            binding?.progressMain?.visibility = View.GONE

            if (it == null) {
                binding?.error?.visibility = View.VISIBLE
            } else {
                binding?.error?.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}