package com.aforeapps.learnenglish.utils

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.aforeapps.learnenglish.R

fun NavController.getNavigationAnimation(): NavOptions {
    val navBuilder = NavOptions.Builder()
    navBuilder.setEnterAnim(R.anim.right_in).setExitAnim(R.anim.left_out)
        .setPopEnterAnim(R.anim.left_in).setPopExitAnim(R.anim.right_out)
    return navBuilder.build()
}

fun View.removeParent() {
    if (this.parent != null) {
        (this.parent as ViewGroup).removeView(
            this
        )
    }
}