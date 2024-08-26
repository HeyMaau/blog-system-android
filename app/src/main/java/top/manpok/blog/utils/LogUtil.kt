package top.manpok.blog.utils

import android.util.Log
import top.manpok.blog.BuildConfig
import top.manpok.blog.base.BaseApplication
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

object LogUtil {

    private const val TAG = "LogUtil"

    private val logBuffer: MutableList<String> = mutableListOf()
    private const val BUFFER_SIZE: Int = 10
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("MM-dd hh:mm:ss:SSS")

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
        add2LogBuffer(tag, msg, "I")
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg)
        }
        add2LogBuffer(tag, msg, "W")
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
        add2LogBuffer(tag, msg, "E")
    }

    @Synchronized
    private fun add2LogBuffer(tag: String, msg: String, level: String) {
        val date = dateFormatter.format(Date())
        val logText = "$date $level $tag $msg\n"
        logBuffer.add(logText)
        if (logBuffer.size >= BUFFER_SIZE) {
            writeLog2File()
        }
    }

    @Synchronized
    private fun writeLog2File() {
        val rootPath = BaseApplication.getApplication().getExternalFilesDir("log")
        val file = File(rootPath, "log.txt")
        val writer = FileWriter(file, true)
        val bufferedWriter = BufferedWriter(writer)
        bufferedWriter.write("abc")
        try {
            logBuffer.forEach {
                writer.write(it)
            }
            logBuffer.clear()
        } catch (e: Exception) {
            Log.e(TAG, "writeLog2File error: $e")
        } finally {
            try {
                writer.close()
            } catch (e: Exception) {
                Log.e(TAG, "writeLog2File close error: $e")
            }
        }
    }

}