package top.manpok.blog.base

import android.app.Application

class BaseApplication : Application() {

    companion object {

        private lateinit var sApplication: Application
        fun getApplication(): Application = sApplication
    }

    override fun onCreate() {
        super.onCreate()
        sApplication = this
    }
}