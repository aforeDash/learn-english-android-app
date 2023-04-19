package com.aforeapps.learnenglish.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.IOException

class AudioRecorder(
    private val context: Context,
) {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var audioFilePath: String? = null

    fun startRecording(fileName: String) {
        if (!isPermissionGranted()) {
            return
        }

        if (player != null) {
            player?.stop()
            player?.release()
            player = null
        }

        val path = getAudioFilePath(fileName)

        Log.d("simul", "path to record: $path")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = MediaRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(path)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                //            setAudioChannels(1)
                //            setAudioSamplingRate(44100)
                //            setAudioEncodingBitRate(192000)

                try {
                    prepare()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                start()
            }
        } else {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(path)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                //            setAudioChannels(1)
                //            setAudioSamplingRate(44100)
                //            setAudioEncodingBitRate(192000)

                try {
                    prepare()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                start()
            }
        }
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    fun playAudio(fileName: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource(getAudioFilePath("$fileName.mp3"))
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            start()
        }
    }

    fun stopAudio() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }

    private fun isPermissionGranted(): Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )

        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    fun getAudioFilePath(fileName: String): String {
        val dir = context.cacheDir
        audioFilePath = "${dir?.absolutePath}/${fileName}.mp3"
        return audioFilePath!!
    }
}
