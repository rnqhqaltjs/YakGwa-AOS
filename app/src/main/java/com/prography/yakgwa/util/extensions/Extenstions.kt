package com.prography.yakgwa.util.extensions

import android.app.Activity
import android.view.View
import android.view.WindowManager

fun View.hide(activity: Activity) {
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    visibility = View.GONE
}

fun View.show(activity: Activity) {
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    visibility = View.VISIBLE
}