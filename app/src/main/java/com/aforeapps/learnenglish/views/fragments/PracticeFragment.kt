package com.aforeapps.learnenglish.views.fragments

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aforeapps.learnenglish.R
import com.aforeapps.learnenglish.data.DataConverter
import com.aforeapps.learnenglish.data.models.VideoItem
import com.aforeapps.learnenglish.databinding.FragmentPracticeBinding
import com.aforeapps.learnenglish.utils.AudioRecorder
import com.aforeapps.learnenglish.viewmodels.PlayerViewModel
import com.aforeapps.learnenglish.views.activities.hideNavView
import com.aforeapps.learnenglish.views.activities.showNavView
import com.aforeapps.learnenglish.views.adapters.DATA
import com.aforeapps.learnenglish.views.adapters.SentencePracticeListAdapter
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.File
import kotlin.math.roundToLong

class PracticeFragment : Fragment(), YouTubePlayerListener {

    private lateinit var viewModel: PlayerViewModel

    private var _binding: FragmentPracticeBinding? = null
    private val binding get() = _binding

    private var videoItem: VideoItem? = null

    private val indexMap = HashMap<Long, Int>()

    private var sentenceVerticalListAdapter: SentencePracticeListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var verticalLayoutManager: LinearLayoutManager? = null

    private lateinit var youtubePlayerView: YouTubePlayerView

    private var youTubePlayer: YouTubePlayer? = null

    private var isPlaying = false
    private var isReady = false

    private var audioRecorder: AudioRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    private var countDownTimer: CountDownTimer? = null
    private var toneGenerator: ToneGenerator? = null

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // do something
            if (isGranted) {
                Log.d("Permission", "Granted")
            } else {
                when {
                    shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.
                        // showInContextUI(...)
                        showSnackBar(
                            "We need the permission to record your audio"
                        )
                    }
                    else -> {
                        showSnackBar(
                            "Please go to settings and enable the permission"
                        )
                    }
                }
            }
        }


    private fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("OK") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

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

        audioRecorder = AudioRecorder(requireActivity())
        mediaPlayer = MediaPlayer()
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        initPermission()
    }

    private fun initPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // You can use the API that requires the permission.
            requestPermission.launch(
                Manifest.permission.RECORD_AUDIO
            )
        }
    }

    private fun collectVideoItem() {
        Handler(Looper.getMainLooper()).post {
            binding?.progressMain?.visibility = View.VISIBLE
            viewModel.getRandomVideoData().observe(viewLifecycleOwner) { vi ->
                vi?.let {
                    videoItem = vi
                    binding?.progressBar?.max = 0
                    mapIndex()
                    prepareSentence(vi)
                    initNow()
                }
                binding?.progressMain?.visibility = View.GONE
            }
        }
    }

    private fun prepareSentence(it: VideoItem) {
        sentenceVerticalListAdapter = SentencePracticeListAdapter(requireContext(),
            it.sentences,
            it.endTimeSec,
            object : SentencePracticeListAdapter.OnItemClickListener {
                override fun onItemClick(
                    position: Int,
                    duration: Long,
                    progressBar: ProgressBar,
                    btRecord: Button,
                    tvCount: TextView,
                ) {
                    countDownTimer = object : CountDownTimer(3000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            tvCount.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE

                            tvCount.text = ((millisUntilFinished / 1000) + 1).toString()
                            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
                        }

                        override fun onFinish() {
                            tvCount.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                            progressBar.max = duration.toInt()
                            progressBar.progress = 0
                            val progressAnimator = ObjectAnimator.ofInt(
                                progressBar, "progress", 0, duration.toInt()
                            )
                            progressAnimator.duration = duration
                            progressAnimator.start()


                            audioRecorder?.startRecording(videoItem?.sentences?.get(position)?.id.toString())
                            Handler(Looper.getMainLooper()).postDelayed({
                                context?.let { context ->
                                    btRecord.text = context.getString(R.string.record)
                                    btRecord.isEnabled = true

                                    audioRecorder?.stopRecording()
                                    progressAnimator.cancel()

                                    it.sentences[position].audioTrack = 1
                                    sentenceVerticalListAdapter?.notifyItemChanged(position)
                                }
                            }, duration)
                        }
                    }.start()
                }

                override fun onPlayClick(position: Int) {
                    binding?.rvSentenceVerticalList?.scrollToPosition(position)
                    videoItem?.let { videoItem ->
                        onPositionChanged(videoItem, position)
                    }
                }
            })

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        verticalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)
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

        binding?.view?.setOnClickListener {
            if (isPlaying) {
                youTubePlayer?.pause()
            } else {
                youTubePlayer?.play()
            }
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
            binding?.rvSentenceVerticalList?.apply {
                adapter = sentenceVerticalListAdapter
                layoutManager = this@PracticeFragment.verticalLayoutManager
                itemAnimator = null
            }
        }
    }

    private fun onPositionChanged(videoItem: VideoItem, position: Int) {
        youTubePlayer?.pause()
        val time = videoItem.sentences[position].startTimeSec
        youTubePlayer?.seekTo(time.toFloat())
        //scrollPositionUpdated(position)
        binding?.progressBar?.progress = (time).toInt()
        youTubePlayer?.play()
    }

    private fun scrollPositionUpdated(position: Int) {
        binding?.rvSentenceVerticalList?.post {
            sentenceVerticalListAdapter?.activate(position)
            Handler(Looper.getMainLooper()).post {
                videoItem?.let { videoItem ->
                    //binding?.rvSentenceVerticalList?.smoothScrollToPosition(position)

                    audioRecorder?.let {
                        val path = it.getAudioFilePath(videoItem.sentences[position].id.toString())
                        Log.d("simul", "path to play: $path")
                        val file = File(path)
                        if (file.exists() && videoItem.sentences[position].audioTrack == 1) {
                            mediaPlayer?.reset()
                            mediaPlayer?.setDataSource(file.absolutePath)
                            mediaPlayer?.prepare()
                            mediaPlayer?.start()
                            youTubePlayer?.mute()
                        } else {
                            youTubePlayer?.unMute()
                        }
                    }
                }
            }
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

                val iFramePlayerOptions: IFramePlayerOptions =
                    IFramePlayerOptions.Builder().controls(0).rel(0).ivLoadPolicy(0).ccLoadPolicy(0)
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

        mapIndex()
    }

    private fun mapIndex() {
        indexMap.clear()
        videoItem?.let {
            it.sentences.forEachIndexed { index, sentence ->
                indexMap[sentence.startTimeSec] = index
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countDownTimer?.cancel()
        mediaPlayer?.release()
        toneGenerator?.release()
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

    private var lastSec = -1f;
    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

        if (second == lastSec) {
            lastSec = second
            return
        }
        lastSec = second


        videoItem?.let { videoItem ->
            val index = indexMap[(second).roundToLong()]
            if (index != null && index != sentenceVerticalListAdapter!!.getCurrentPosition()) {
                scrollPositionUpdated(index)
            } else if (index == sentenceVerticalListAdapter!!.getCurrentPosition()) {
                scrollPositionUpdated(sentenceVerticalListAdapter!!.getCurrentPosition() - 1)
                youTubePlayer.seekTo((videoItem.sentences[sentenceVerticalListAdapter!!.getCurrentPosition() - 1].startTimeSec).toFloat())
            }

            if (second >= videoItem.endTimeSec) {
                youTubePlayer.pause()
            }

            binding?.progressBar?.progress = (second - videoItem.startTimeSec).toInt()
        }

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
        isReady = true
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        when (state) {
            PlayerConstants.PlayerState.PLAYING -> {
                binding?.ivPlay?.visibility = View.GONE
                isPlaying = true
            }
            PlayerConstants.PlayerState.PAUSED -> {
                binding?.ivPlay?.visibility = View.VISIBLE
                isPlaying = false
                mediaPlayer?.pause()
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