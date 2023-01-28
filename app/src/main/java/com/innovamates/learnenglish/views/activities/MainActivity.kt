package com.innovamates.learnenglish.views.activities

import android.content.Context
import android.net.ConnectivityManager.OnNetworkActiveListener
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.youtube.player.MyYoutubePlayer
import com.innovamates.learnenglish.R
import com.innovamates.learnenglish.databinding.ActivityMainBinding
import com.innovamates.learnenglish.utils.NotificationBarMovedListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var navView: BottomNavigationView
    lateinit var myYoutubePlayer: MyYoutubePlayer

    var notificationBarMovedListener: NotificationBarMovedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        initYoutubePlayer()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initYoutubePlayer() {
        myYoutubePlayer = MyYoutubePlayer(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        myYoutubePlayer.youtubePlayer.release()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) { // hasFocus is true
            // the user pushed the notification bar back to the top
            Log.i("Tag", "Notification bar is pushed up")
        } else { // hasFocus is false
            // the user pulls down the notification bar
            Log.i("Tag", "Notification bar is pulled down")
            notificationBarMovedListener?.onNotificationPullDown()
        }
        super.onWindowFocusChanged(hasFocus)
    }


}

fun Context.getMyYoutubePlayer(): MyYoutubePlayer? {
    if (this is MainActivity) {
        return myYoutubePlayer
    }
    return null
}

fun Context.hideNavView() {
    if (this is MainActivity) {
        navView.visibility = View.GONE
    }
}

fun Context.showNavView() {
    if (this is MainActivity) {
        navView.visibility = View.VISIBLE
    }
}