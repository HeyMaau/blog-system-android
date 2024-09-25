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
    private var screenWidth: Int = 0
    private var reverse = false

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
        screenWidth = activity.resources.displayMetrics.widthPixels
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
        expandState = ExpandState.Collapse
        reverse = false
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
        val icCtrl = floatingWindow.findViewById<ImageView>(R.id.ic_ctrl)

        val collapseRunnable = Runnable {
            setWidthAnimation(activity, floatingWindow, 120F, 50F)
            expandState = ExpandState.Collapse
        }

        val collapseRunnableReverse = Runnable {
            setWidthAnimationReverse(
                activity,
                floatingWindow,
                120F,
                50F
            )
            expandState = ExpandState.Collapse
        }


        floatingWindow.setOnTouchListener(object : OnTouchListener {

            private var x = 0
            private var y = 0
            private var actionDownTime = 0L

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.rawX.toInt()
                        y = event.rawY.toInt()
                        actionDownTime = System.currentTimeMillis()
                        removeFloatingWindowRunnable(
                            floatingWindow,
                            collapseRunnable,
                            collapseRunnableReverse
                        )
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

                    MotionEvent.ACTION_UP -> {
                        if (System.currentTimeMillis() - actionDownTime <= 100) {
                            onFloatingWindowClick(
                                activity,
                                floatingWindow,
                                collapseRunnable,
                                collapseRunnableReverse
                            )
                            return true
                        }
                        val floatingWindowX = floatingWindow.x
                        if (floatingWindowX < 0 || floatingWindowX <= screenWidth / 2) {
                            reverseView(false, floatingWindow, icCtrl)
                            reverse = false
                            val valueAnimator = ValueAnimator.ofFloat(floatingWindowX, 0F)
                            valueAnimator.duration = 200
                            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
                            valueAnimator.addUpdateListener {
                                floatingWindow.x = it.animatedValue as Float
                            }
                            valueAnimator.start()
                            if (expandState == ExpandState.Expand) {
                                floatingWindow.postDelayed(collapseRunnable, 2000)
                            }
                        }
                        if (floatingWindowX > screenWidth / 2) {
                            reverseView(true, floatingWindow, icCtrl)
                            reverse = true
                            val valueAnimator = ValueAnimator.ofFloat(
                                floatingWindowX,
                                (screenWidth - floatingWindow.width).toFloat()
                            )
                            valueAnimator.duration = 200
                            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
                            valueAnimator.addUpdateListener {
                                floatingWindow.x = it.animatedValue as Float
                            }
                            valueAnimator.start()
                            if (expandState == ExpandState.Expand) {
                                floatingWindow.postDelayed(collapseRunnableReverse, 2000)
                            }
                        }
                    }

                    else -> {

                    }
                }
                return true
            }
        })
        val icClose = floatingWindow.findViewById<ImageView>(R.id.ic_close)
        icClose.setOnClickListener {
            GlobalViewModelManager.audioViewModel.playOrPauseAudio()
            removeFloatingWindow(activity)
        }

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
                removeFloatingWindowRunnable(
                    floatingWindow,
                    collapseRunnable,
                    collapseRunnableReverse
                )
                floatingWindow.postDelayed(
                    if (reverse) collapseRunnableReverse else collapseRunnable,
                    2000
                )
            }
        }
    }

    private fun reverseView(reverse: Boolean, vararg view: View) {
        view.forEach {
            it.rotationY = if (reverse) 180F else 0F
        }
    }

    private fun removeFloatingWindowRunnable(view: View, vararg runnable: Runnable) {
        runnable.forEach {
            view.removeCallbacks(it)
        }
    }

    private fun onFloatingWindowClick(
        activity: Activity,
        view: View,
        collapseRunnable: Runnable,
        collapseRunnableReverse: Runnable
    ) {
        if (expandState == ExpandState.Expand) {
            val intent = Intent(activity, AudioPlayerActivity::class.java)
            activity.startActivity(intent)
            return
        }
        if (reverse) {
            setWidthAnimationReverse(activity, view, 50F, 120F)
        } else {
            setWidthAnimation(activity, view, 50F, 120F)
        }
        expandState = ExpandState.Expand
        view.postDelayed(if (reverse) collapseRunnableReverse else collapseRunnable, 2000)
    }

    private fun setWidthAnimation(activity: Activity, view: View, start: Float, end: Float) {
        val valueAnimator = ValueAnimator.ofInt(
            DensityUtil.dpToPx(activity, start),
            DensityUtil.dpToPx(activity, end)
        )
        valueAnimator.duration = 200
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            view.layoutParams.width = it.animatedValue as Int
            view.requestLayout()
        }
        valueAnimator.start()
    }

    private fun setWidthAnimationReverse(activity: Activity, view: View, start: Float, end: Float) {
        val valueAnimator = ValueAnimator.ofInt(
            DensityUtil.dpToPx(activity, start),
            DensityUtil.dpToPx(activity, end)
        )
        valueAnimator.duration = 200
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        val viewOriginalX = view.x
        valueAnimator.addUpdateListener {
            view.layoutParams.width = it.animatedValue as Int
            view.x = viewOriginalX - (it.animatedValue as Int - DensityUtil.dpToPx(activity, start))
            view.requestLayout()
        }
        valueAnimator.start()
    }
}
