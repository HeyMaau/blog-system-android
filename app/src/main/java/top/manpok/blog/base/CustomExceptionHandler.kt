package top.manpok.blog.base

import android.os.Process
import top.manpok.blog.utils.LogUtil
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Date

class CustomExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        val stringWriter = StringWriter()
        e.printStackTrace(PrintWriter(stringWriter))
        LogUtil.writeCrashLog2File(
            "----------------------------${
                LogUtil.fileDateFormatter.format(
                    Date()
                )
            } ${Process.myPid()}-${Process.myTid()} 发生了Crash----------------------------\n$stringWriter"
        )
        BaseApplication.getDefaultUncaughtExceptionHandler().uncaughtException(t, e)
    }
}