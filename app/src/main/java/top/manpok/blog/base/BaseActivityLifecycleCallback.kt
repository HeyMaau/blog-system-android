package top.manpok.blog.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import top.manpok.blog.activity.AudioPlayerActivity
import top.manpok.blog.view.AudioFloatingWindowManager
import top.manpok.blog.viewmodel.AudioViewModel
import top.manpok.blog.viewmodel.GlobalViewModelManager

class BaseActivityLifecycleCallback : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        MainScope().launch {
            val playState = GlobalViewModelManager.audioViewModel.playState.first()
            if (activity !is AudioPlayerActivity && playState == AudioViewModel.PlayState.Playing) {
                AudioFloatingWindowManager.addFloatingWindow(activity)
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        AudioFloatingWindowManager.removeFloatingWindow(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}