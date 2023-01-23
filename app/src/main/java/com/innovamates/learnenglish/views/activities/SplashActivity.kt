package com.innovamates.learnenglish.views.activities

import android.app.ActionBar
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.innovamates.learnenglish.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenInitialization()
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            val decorView = window.decorView

            @Suppress("DEPRECATION")
//            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = uiOptions
            val actionBar: ActionBar? = actionBar
            actionBar?.hide()
        }
    }

    private fun splashScreenInitialization() {
        installSplashScreen()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Splash screen icon animation modification if needed
        }

        var dataReady = false

        Handler(Looper.getMainLooper()).postDelayed({
            if (!dataReady) {
                dataReady = true
            }
        }, 1000)

        // Set up an OnPreDrawListener to the root view.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (dataReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)

                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            })


    }

}