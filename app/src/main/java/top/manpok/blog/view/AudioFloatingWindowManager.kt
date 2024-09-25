package top.manpok.blog.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.activity.AudioPlayerActivity
import top.manpok.blog.utils.DensityUtil
import top.manpok.blog.viewmodel.AudioViewModel
import top.manpok.blog.viewmodel.GlobalViewModelManager

object AudioFloatingWindowManager {

    private var expandState: ExpandState = ExpandState.Collapse

    sealed class ExpandState {
        data object Expand : ExpandState()
        data object Collapse : ExpandState()
    }

    fun addFloatingWindow(activity: Activity) {
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val floatingWindow =
            LayoutInflater.from(activity).inflate(R.layout.audio_floating_window, null)
        val icMusic = floatingWindow.findViewById<ImageView>(R.id.ic_music)
        val rotationAnimator = setRotationAnimator(icMusic)
        setListener(activity, floatingWindow, rotationAnimator)
        floatingWindow.y = (activity.resources.displayMetrics.heightPixels / 2).toFloat()
        val layoutParams = ViewGroup.LayoutParams(
            DensityUtil.dpToPx(activity, 50F),
            DensityUtil.dpToPx(activity, 40F)
        )
        floatingWindow.id = R.id.id_floating_window
        contentView.addView(floatingWindow, layoutParams)
    }

    fun removeFloatingWindow(activity: Activity) {
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val floatingWindow = contentView.findViewById<View>(R.id.id_floating_window)
        if (floatingWindow != null) {
            contentView.removeView(floatingWindow)
        }
    }

    private fun setRotationAnimator(view: View): ObjectAnimator {
        val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = 3000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.start()
        return objectAnimator
    }

    private fun setListener(
        activity: Activity,
        floatingWindow: View,
        rotationAnimator: ObjectAnimator
    ) {
        val collapseRunnable = Runnable {
            setWidthAnimation(activity, floatingWindow, 120F, 50F)
            expandState = ExpandState.Collapse
        }

        floatingWindow.setOnClickListener {
            if (expandState == ExpandState.Expand) {
                val intent = Intent(activity, AudioPlayerActivity::class.java)
                activity.startActivity(intent)
                return@setOnClickListener
            }
            setWidthAnimation(activity, floatingWindow, 50F, 120F)
            expandState = ExpandState.Expand
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
        val icClose = floatingWindow.findViewById<ImageView>(R.id.ic_close)
        icClose.setOnClickListener {
            GlobalViewModelManager.audioViewModel.playOrPauseAudio()
            removeFloatingWindow(activity)
        }

        val icCtrl = floatingWindow.findViewById<ImageView>(R.id.ic_ctrl)
        icCtrl.setOnClickListener {
            GlobalViewModelManager.audioViewModel.playOrPauseAudio()
            MainScope().launch {
                val playState = GlobalViewModelManager.audioViewModel.playState.first()
                icCtrl.setImageResource(if (playState == AudioViewModel.PlayState.Playing) R.drawable.ic_pause_floating else R.drawable.ic_play_floating)
                if (playState != AudioViewModel.PlayState.Playing) {
                    rotationAnimator.pause()
                } else {
                    rotationAnimator.resume()
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
