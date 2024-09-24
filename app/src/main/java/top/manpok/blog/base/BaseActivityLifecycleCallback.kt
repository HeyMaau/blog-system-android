package top.manpok.blog.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import top.manpok.blog.R

class BaseActivityLifecycleCallback : ActivityLifecycleCallbacks {

    private lateinit var floatingWindow: View

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        addFloatingWindow(activity)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    private fun addFloatingWindow(activity: Activity) {
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val floatingWindow =
            LayoutInflater.from(activity).inflate(R.layout.audio_floating_window, null)
        floatingWindow.setOnClickListener { }
        floatingWindow.setOnTouchListener(object : OnTouchListener {

            private var x = 0
            private var y = 0

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.rawX.toInt()
                        y = event.rawY.toInt()

                    }

                    MotionEvent.ACTION_MOVE -> {
                        val nowX = event.rawX.toInt()
                        val nowY = event.rawY.toInt()
                        val movedX = nowX - x
                        val movedY = nowY - y
                        x = nowX
                        y = nowY
                        floatingWindow.apply {
                            x += movedX
                            y += movedY
                        }
                    }

                    else -> {

                    }
                }
                return false
            }
        })
        floatingWindow.x = 500F
        floatingWindow.y = 500F
        val layoutParams = ViewGroup.LayoutParams(
            200,
            200
        )
        contentView.addView(floatingWindow, layoutParams)
    }
}