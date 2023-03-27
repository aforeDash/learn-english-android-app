package com.aforeapps.learnenglish.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.DataConverter
import com.aforeapps.learnenglish.data.models.SubCategory
import com.aforeapps.learnenglish.data.models.VideoItem
import com.aforeapps.learnenglish.databinding.FragmentVideoListBinding
import com.aforeapps.learnenglish.utils.*
import com.aforeapps.learnenglish.viewmodels.VideoListViewModel
import com.aforeapps.learnenglish.views.activities.hideNavView
import com.aforeapps.learnenglish.views.activities.showNavView
import com.aforeapps.learnenglish.views.adapters.DATA
import com.aforeapps.learnenglish.views.adapters.VideoListAdapter

class VideoListFragment : Fragment() {

    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding

    private val videoListViewModel: VideoListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            VideoListViewModel.Factory(activity.application)
        )[VideoListViewModel::class.java]
    }

    private lateinit var videoListAdapter: VideoListAdapter
    private var subCategory: SubCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            videoListAdapter = VideoListAdapter(
                it,
                arrayListOf(),
                object : VideoListAdapter.VideoItemClickListener {
                    override fun onVideoItemClicked(videoItem: VideoItem) {
                        videoListViewModel.getFullVideoData(videoItem.id)
                            .observe(viewLifecycleOwner) { vi ->
                                vi?.let {
                                    val bundle = Bundle()
                                    bundle.putString(DATA, DataConverter.fromVideoItem(vi))

                                    val navController =
                                        requireActivity().findNavController(R.id.nav_host_fragment_activity_main)

                                    navController.navigate(
                                        R.id.navigation_player_fragment,
                                        bundle,
                                        navController.getNavigationAnimation()
                                    )
                                }
                            }
                    }
                }, this
            )
            subCategory = arguments?.getString(DATA)?.let { data ->
                DataConverter.toSubCategory(data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvVideoList?.apply {
            adapter = videoListAdapter
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

        observerData()
    }

    private fun observerData() {
        binding?.progressMain?.visibility = View.VISIBLE
        subCategory?.let {
            videoListViewModel.getVideoData(it.id).observe(viewLifecycleOwner) { videoData ->
                videoData?.videoItems?.forEachIndexed { index, videoItem ->
                    videoListAdapter.addItem(videoItem, index)
                } ?: run {
                    Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show()
                }
                binding?.progressMain?.visibility = View.GONE

                if (videoData.videoItems == null ||
                    videoData.videoItems.isEmpty()
                ) {
                    binding?.error?.visibility = View.VISIBLE
                } else {
                    binding?.error?.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        context?.hideNavView()
        super.onStart()
    }

    override fun onStop() {
        context?.showNavView()
        super.onStop()
    }
}