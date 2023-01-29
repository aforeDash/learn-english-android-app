package com.innovamates.learnenglish.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.innovamates.learnenglish.databinding.FragmentVideoListBinding
import com.innovamates.learnenglish.utils.OnSnapPositionChangeListener
import com.innovamates.learnenglish.utils.SnapHelper
import com.innovamates.learnenglish.utils.SnapOnScrollListener
import com.innovamates.learnenglish.utils.attachSnapHelperWithListener
import com.innovamates.learnenglish.viewmodels.VideoListViewModel
import com.innovamates.learnenglish.views.activities.hideNavView
import com.innovamates.learnenglish.views.activities.showNavView
import com.innovamates.learnenglish.views.adapters.VideoListAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            videoListAdapter = VideoListAdapter(it, arrayListOf())
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
        }

        binding?.rvVideoList?.attachSnapHelperWithListener(SnapHelper().snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {}
            })

        observerData()
    }

    private fun observerData() {
        videoListViewModel.videoItemList.observe(viewLifecycleOwner) {
            it.forEachIndexed { index, videoItem ->
                videoListAdapter.addItem(videoItem, index)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        videoListViewModel.videoItemList.removeObservers(viewLifecycleOwner)
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