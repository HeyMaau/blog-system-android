package top.manpok.blog.base

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.activity.AudioPlayerActivity
import top.manpok.blog.utils.DensityUtil
import top.manpok.blog.view.AudioFloatingWindowManager
import top.manpok.blog.viewmodel.AudioViewModel
import top.manpok.blog.viewmodel.GlobalViewModelManager

class BaseActivityLifecycleCallback : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        CoroutineScope(Dispatchers.Main).launch {
            val playState = GlobalViewModelManager.audioViewModel.playState.first()
            if (activity !is AudioPlayerActivity && playState == AudioViewModel.PlayState.Playing) {
                addFloatingWindow(activity)
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        removeFloatingWindow(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    private fun removeFloatingWindow(activity: Activity) {
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val floatingWindow = contentView.findViewById<View>(R.id.id_floating_window)
        if (floatingWindow != null) {
            contentView.removeView(floatingWindow)
        }
    }

    private fun addFloatingWindow(activity: Activity) {
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val floatingWindow =
            LayoutInflater.from(activity).inflate(R.layout.audio_floating_window, null)
        val collapseRunnable = Runnable {
            setWidthAnimation(activity, floatingWindow, 120F, 50F)
            AudioFloatingWindowManager.expandState = AudioFloatingWindowManager.ExpandState.Collapse
        }
        floatingWindow.setOnClickListener {
            if (AudioFloatingWindowManager.expandState == AudioFloatingWindowManager.ExpandState.Expand) {
                val intent = Intent(activity, AudioPlayerActivity::class.java)
                activity.startActivity(intent)
                return@setOnClickListener
            }
            setWidthAnimation(activity, floatingWindow, 50F, 120F)
            AudioFloatingWindowManager.expandState = AudioFloatingWindowManager.ExpandState.Expand
            floatingWindow.postDelayed(collapseRunnable, 2000)
        }
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
        floatingWindow.id = R.id.id_floating_window
        contentView.addView(floatingWindow, layoutParams)
        val icMusic = floatingWindow.findViewById<ImageView>(R.id.ic_music)
        val objectAnimator = ObjectAnimator.ofFloat(icMusic, "rotation", 0f, 360f)
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = 3000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.start()

        val icClose = floatingWindow.findViewById<ImageView>(R.id.ic_close)
        icClose.setOnClickListener {
            GlobalViewModelManager.audioViewModel.playOrPauseAudio()
            contentView.removeView(floatingWindow)
        }

        val icCtrl = floatingWindow.findViewById<ImageView>(R.id.ic_ctrl)
        icCtrl.setOnClickListener {
            GlobalViewModelManager.audioViewModel.playOrPauseAudio()
            CoroutineScope(Dispatchers.Main).launch {
                val playState = GlobalViewModelManager.audioViewModel.playState.first()
                icCtrl.setImageResource(if (playState == AudioViewModel.PlayState.Playing) R.drawable.ic_pause_floating else R.drawable.ic_play_floating)
                if (playState != AudioViewModel.PlayState.Playing) {
                    objectAnimator.pause()
                } else {
                    objectAnimator.resume()
                }
                floatingWindow.removeCallbacks(collapseRunnable)
                floatingWindow.postDelayed(collapseRunnable, 2000)
            }
        }
    }

    private fun setWidthAnimation(activity: Activity, view: View, start: Float, end: Float) {
        val valueAnimator = ValueAnimator.ofInt(
            DensityUtil.dpToPx(activity, start),
            DensityUtil.dpToPx(activity, end)
        )
        valueAnimator.duration = 200
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            view.layoutParams.width = valueAnimator.animatedValue as Int
            view.requestLayout()
        }
        valueAnimator.start()
    }
}