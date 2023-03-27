package com.aforeapps.learnenglish.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.DataConverter
import com.aforeapps.learnenglish.data.models.VideoItem
import com.aforeapps.learnenglish.databinding.FragmentPlayerBinding
import com.aforeapps.learnenglish.utils.*
import com.aforeapps.learnenglish.viewmodels.PlayerViewModel
import com.aforeapps.learnenglish.views.activities.hideNavView
import com.aforeapps.learnenglish.views.activities.showNavView
import com.aforeapps.learnenglish.views.adapters.DATA
import com.aforeapps.learnenglish.views.adapters.SentenceListAdapter
import com.aforeapps.learnenglish.views.adapters.SentenceVerticalListAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlin.math.roundToLong


class PlayerFragment : Fragment(), YouTubePlayerListener {

    private lateinit var viewModel: PlayerViewModel

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding

    private var videoItem: VideoItem? = null

    private val indexMap = HashMap<Long, Int>()

    private var sentenceListAdapter: SentenceListAdapter? = null
    private var sentenceVerticalListAdapter: SentenceVerticalListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var verticalLayoutManager: LinearLayoutManager? = null

    private lateinit var youtubePlayerView: YouTubePlayerView

    private var youTubePlayer: YouTubePlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupVideoData()

        videoItem?.let {
            prepareSentence(it)
        }
    }

    private fun collectVideoItem() {
        binding?.progressMain?.visibility = View.VISIBLE
        viewModel.getRandomVideoData()
            .observe(viewLifecycleOwner) { vi ->
                vi?.let {
                    videoItem = vi
                    prepareSentence(vi)
                    initNow()
                }
                binding?.progressMain?.visibility = View.GONE
            }
    }

    private fun prepareSentence(it: VideoItem) {
        sentenceListAdapter = SentenceListAdapter(requireContext(), it.sentences)
        sentenceVerticalListAdapter =
            SentenceVerticalListAdapter(requireContext(), it.sentences)

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        verticalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoItem?.let {
            initNow()
        } ?: run {
            initFab()
            collectVideoItem()
        }
    }

    private fun initFab() {
        binding?.fab?.visibility = View.VISIBLE
        binding?.fab?.setOnClickListener {
            collectVideoItem()
        }
    }

    private fun initNow() {
        setupVideoPlayer()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        context?.let {
            binding?.rvSentenceList?.apply {
                adapter = sentenceListAdapter
                layoutManager = this@PlayerFragment.layoutManager
                itemAnimator = null
            }

            binding?.rvSentenceVerticalList?.apply {
                adapter = sentenceVerticalListAdapter
                layoutManager = this@PlayerFragment.verticalLayoutManager
                itemAnimator = null
            }

            binding?.rvSentenceList?.attachSnapHelperWithListener(SnapHelper().snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
                object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {
                        videoItem?.let {
                            onPositionChanged(it, position)
                        }
                    }
                })

            binding?.ibBack?.setOnClickListener {
                layoutManager?.let {
                    val position = it.findFirstCompletelyVisibleItemPosition()
                    binding?.rvSentenceList?.scrollToPosition(position - 1)
                    videoItem?.let { videoItem ->
                        onPositionChanged(videoItem, position - 1)
                    }
                }
            }

            binding?.ibForward?.setOnClickListener {
                layoutManager?.let {
                    val position = it.findFirstCompletelyVisibleItemPosition()
                    binding?.rvSentenceList?.scrollToPosition(position + 1)
                    videoItem?.let { videoItem ->
                        onPositionChanged(videoItem, position + 1)
                    }
                }
            }
        }
    }

    private fun onPositionChanged(videoItem: VideoItem, position: Int) {
        youTubePlayer?.pause()
        val time = videoItem.sentences[position].startTimeSec
        youTubePlayer?.seekTo(time.toFloat())
        scrollPositionUpdated(position)
        binding?.progressBar?.progress = (time).toInt()
    }

    private fun scrollPositionUpdated(position: Int) {
        when (position) {
            0 -> {
                binding?.ibBack?.visibility = View.GONE
                binding?.ibForward?.visibility = View.VISIBLE
            }
            videoItem!!.sentences.size - 1 -> {
                binding?.ibBack?.visibility = View.VISIBLE
                binding?.ibForward?.visibility = View.GONE
            }
            else -> {
                binding?.ibBack?.visibility = View.VISIBLE
                binding?.ibForward?.visibility = View.VISIBLE
            }
        }

        binding?.rvSentenceVerticalList?.scrollToPosition(position)
        binding?.rvSentenceVerticalList?.post {
            sentenceVerticalListAdapter?.activate(position)
        }
    }

    private fun initVideoPlaying() {
        youTubePlayer?.let { youtubePlayer ->
            videoItem?.let { videoItem ->
                binding?.progressBar?.max = ((videoItem.endTimeSec - videoItem.startTimeSec))
                youtubePlayer.cueVideo(videoItem.youtube_id, videoItem.startTimeSec.toFloat())
            }
        }
    }

    private fun setupVideoPlayer() {
        view?.let {
            if (youTubePlayer == null) {
                youtubePlayerView = requireView().findViewById(R.id.yt_player)

                val iFramePlayerOptions: IFramePlayerOptions = IFramePlayerOptions.Builder()
                    .controls(0)
                    .rel(0)
                    .ivLoadPolicy(0)
                    .ccLoadPolicy(0)
                    .build()

                lifecycle.addObserver(youtubePlayerView)

                youtubePlayerView.initialize(this, true, iFramePlayerOptions)
            } else {
                initVideoPlaying()
            }
        }
    }

    private fun setupVideoData() {
        videoItem = arguments?.let { it ->
            it.getString(DATA)?.let {
                DataConverter.toVideoItem(it)
            }
        }

        videoItem?.let {
            it.sentences.forEachIndexed { index, sentence ->
                indexMap[sentence.startTimeSec] = index
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        arguments?.let {
            context?.hideNavView()
        }
        super.onStart()
    }

    override fun onStop() {
        arguments?.let {
            context?.showNavView()
        }
        super.onStop()
    }

    override fun onApiChange(youTubePlayer: YouTubePlayer) {

    }

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

        videoItem?.let { videoItem ->
            val index = indexMap[(second).roundToLong()]
            if (index != null && index != sentenceListAdapter!!.getCurrentPosition()) {
                binding?.rvSentenceList?.scrollToPosition(index)
                scrollPositionUpdated(index)
            } else if (index == sentenceListAdapter!!.getCurrentPosition()) {
                binding?.rvSentenceList?.scrollToPosition(sentenceListAdapter!!.getCurrentPosition() - 1)
                scrollPositionUpdated(sentenceListAdapter!!.getCurrentPosition() - 1)
                youTubePlayer.seekTo((videoItem.sentences[sentenceListAdapter!!.getCurrentPosition() - 1].startTimeSec).toFloat())
            }

            if (second >= videoItem.endTimeSec) {
                youTubePlayer.pause()
            }

        }

        binding?.progressBar?.progress = (second).toInt()
    }

    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
        Log.d("simul", error.toString())
    }

    override fun onPlaybackQualityChange(
        youTubePlayer: YouTubePlayer,
        playbackQuality: PlayerConstants.PlaybackQuality,
    ) {

    }

    override fun onPlaybackRateChange(
        youTubePlayer: YouTubePlayer,
        playbackRate: PlayerConstants.PlaybackRate,
    ) {

    }

    override fun onReady(youTubePlayer: YouTubePlayer) {
        this.youTubePlayer = youTubePlayer
        initVideoPlaying()
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        when (state) {
            PlayerConstants.PlayerState.PLAYING -> {
                binding?.ivPlay?.visibility = View.GONE
            }
            PlayerConstants.PlayerState.PAUSED -> {
                binding?.ivPlay?.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }

    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

    }

    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

    }

    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {

    }
}