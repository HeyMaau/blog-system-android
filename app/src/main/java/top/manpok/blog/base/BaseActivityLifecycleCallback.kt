package top.manpok.blog.base

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import top.manpok.blog.R
import top.manpok.blog.utils.DensityUtil

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
        floatingWindow.y = (activity.resources.displayMetrics.heightPixels / 2).toFloat()
        val layoutParams = ViewGroup.LayoutParams(
            DensityUtil.dpToPx(activity, 50F),
            DensityUtil.dpToPx(activity, 40F)
        )
        contentView.addView(floatingWindow, layoutParams)
        val icMusic = floatingWindow.findViewById<ImageView>(R.id.ic_music)
        val animator = ObjectAnimator.ofFloat(icMusic, "rotation", 0f, 360f)
        animator.interpolator = LinearInterpolator()
        animator.duration = 3000
        animator.repeatCount = android.animation.ValueAnimator.INFINITE
        animator.start()
    }
}