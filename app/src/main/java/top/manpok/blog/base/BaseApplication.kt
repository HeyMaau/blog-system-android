package top.manpok.blog.base

import android.app.Application

class BaseApplication : Application() {

    companion object {

        private lateinit var sApplication: Application
        fun getApplication(): Application = sApplication

        private lateinit var sDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler
        fun getDefaultUncaughtExceptionHandler(): Thread.UncaughtExceptionHandler =
            sDefaultUncaughtExceptionHandler
    }

    override fun onCreate() {
        super.onCreate()
        sApplication = this
        sDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()!!
        Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler())
        registerActivityLifecycleCallbacks(BaseActivityLifecycleCallback())
    }
}