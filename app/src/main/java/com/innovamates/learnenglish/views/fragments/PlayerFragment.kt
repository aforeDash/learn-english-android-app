package com.innovamates.learnenglish.views.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.MyYoutubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.innovamates.learnenglish.databinding.FragmentPlayerBinding
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.data.database.typeconverter.DataConverter
import com.innovamates.learnenglish.utils.*
import com.innovamates.learnenglish.viewmodels.PlayerViewModel
import com.innovamates.learnenglish.views.activities.MainActivity
import com.innovamates.learnenglish.views.activities.getMyYoutubePlayer
import com.innovamates.learnenglish.views.activities.hideNavView
import com.innovamates.learnenglish.views.activities.showNavView
import com.innovamates.learnenglish.views.adapters.DATA
import com.innovamates.learnenglish.views.adapters.SentenceListAdapter
import com.innovamates.learnenglish.views.adapters.SentenceVerticalListAdapter
import kotlin.math.roundToLong


class PlayerFragment : Fragment() {

    private lateinit var viewModel: PlayerViewModel

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding

    private var videoItem: VideoItem? = null
    private var animator: ValueAnimator? = null

    private val indexMap = HashMap<Long, Int>()

    private var sentenceListAdapter: SentenceListAdapter? = null
    private var sentenceVerticalListAdapter: SentenceVerticalListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var verticalLayoutManager: LinearLayoutManager? = null

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
            sentenceListAdapter = SentenceListAdapter(requireContext(), it.sentences)
            sentenceVerticalListAdapter =
                SentenceVerticalListAdapter(requireContext(), it.sentences)

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            verticalLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

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

        setupVideoPlayer()
        setupVideoController()
        initVideoPlaying()
        initRecyclerView()

        initNotificationBarMoveListener()
    }

    private fun initNotificationBarMoveListener() {
        activity?.let {
            if (it is MainActivity) {
                it.notificationBarMovedListener = object : NotificationBarMovedListener {
                    override fun onNotificationPullDown() {
                        context?.getMyYoutubePlayer()?.let { myPlayer ->
                            pausePlaying(myPlayer)
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        context?.let {
            binding?.rvSentenceList?.apply {
                adapter = sentenceListAdapter
                layoutManager = this@PlayerFragment.layoutManager
            }

            binding?.rvSentenceVerticalList?.apply {
                adapter = sentenceVerticalListAdapter
                layoutManager = this@PlayerFragment.verticalLayoutManager
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
        context?.getMyYoutubePlayer()?.youtubePlayer?.pause()
        val time = videoItem.sentences[position].startTimeSec * 1000
        context?.getMyYoutubePlayer()?.youtubePlayer?.seekToMillis(time.toInt())
        scrollPositionUpdated(position)
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

    private fun setupVideoController() {
        context?.getMyYoutubePlayer()?.let { myPlayer ->
            myPlayer.youtubePlayerFragment.onClickListener = View.OnClickListener {
                pausePlaying(myPlayer)
            }

            binding?.ivPlay?.setOnClickListener {
                if (!myPlayer.youtubePlayer.isPlaying) {
                    resumePlaying(myPlayer)
                }
            }
        }
    }

    private fun resumePlaying(myPlayer: MyYoutubePlayer) {
        context?.let {
            myPlayer.youtubePlayer.play()
        }
    }

    private fun pausePlaying(myPlayer: MyYoutubePlayer) {
        context?.let {
            if (myPlayer.youtubePlayer.isPlaying) {
                myPlayer.youtubePlayer.pause()
            }
        }
    }

    private fun initVideoPlaying() {
        context?.getMyYoutubePlayer()?.youtubePlayer?.let { youtubePlayer ->
            videoItem?.let { videoItem ->

                animator = ValueAnimator.ofInt(
                    videoItem.videoStartTimeSec.toInt() * 1000,
                    videoItem.videoEndTimeSec.toInt() * 1000
                )
                animator?.duration =
                    (videoItem.videoEndTimeSec - videoItem.videoStartTimeSec) * 1000

                binding?.progressBar?.max =
                    ((videoItem.videoEndTimeSec - videoItem.videoStartTimeSec) * 1000).toInt()

                animator?.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                        super.onAnimationEnd(animation, isReverse)
                        context?.let {
                            youtubePlayer.pause()
                            youtubePlayer.seekToMillis((videoItem.videoStartTimeSec * 1000).toInt())
                        }
                    }

                    override fun onAnimationPause(animation: Animator?) {
                        super.onAnimationPause(animation)
                    }
                })

                animator?.addUpdateListener {
                    youtubePlayer.let {
                        val index =
                            indexMap[(youtubePlayer.currentTimeMillis / 1000f).roundToLong()]
                        if (index != null && index != sentenceListAdapter!!.getCurrentPosition()) {
                            binding?.rvSentenceList?.scrollToPosition(index)
                            scrollPositionUpdated(index)
                        } else if (index == sentenceListAdapter!!.getCurrentPosition()) {
                            binding?.rvSentenceList?.scrollToPosition(sentenceListAdapter!!.getCurrentPosition() - 1)
                            scrollPositionUpdated(sentenceListAdapter!!.getCurrentPosition() - 1)
                            youtubePlayer.seekToMillis((videoItem.sentences[sentenceListAdapter!!.getCurrentPosition() - 1].startTimeSec * 1000).toInt())
                            animator?.currentPlayTime =
                                videoItem.sentences[sentenceListAdapter!!.getCurrentPosition() - 1].startTimeSec * 1000
                        }
                        binding?.progressBar?.progress = youtubePlayer.currentTimeMillis
                    }
                }

                youtubePlayer.setPlaybackEventListener(object : PlaybackEventListener {
                    override fun onPlaying() {
                        binding?.clVideoController?.visibility = View.GONE

                        animator?.let {
                            if (it.isPaused) {
                                it.resume()
                            } else {
                                it.start()
                            }
                        }
                    }

                    override fun onPaused() {
                        binding?.clVideoController?.visibility = View.VISIBLE
                        animator?.pause()
                    }

                    override fun onStopped() {

                    }

                    override fun onBuffering(p0: Boolean) {
                        if (p0) {
                            animator?.pause()
                            binding?.flLoaderView?.visibility = View.VISIBLE
                        } else {
                            binding?.flLoaderView?.visibility = View.GONE
                            animator?.resume()
                        }
                    }

                    override fun onSeekTo(p0: Int) {

                    }

                })
                youtubePlayer.loadVideo(videoItem.videoId)
                youtubePlayer.seekToMillis((videoItem.videoStartTimeSec * 1000).toInt())
            }
        }
    }

    private fun setupVideoPlayer() {
        context?.getMyYoutubePlayer()?.videoView?.let {
            it.removeParent()
            binding?.flPlayerContainer?.addView(it)
        }
    }

    private fun setupVideoData() {
        videoItem = arguments?.let { it ->
            it.getString(DATA)?.let {
                DataConverter.toVideoItemList(it)[0]
            }
        }

        videoItem?.let {
            it.sentences.forEachIndexed { index, sentence ->
                indexMap[sentence.startTimeSec] = index
            }
        }
    }

    override fun onDestroyView() {
        animator?.cancel()
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