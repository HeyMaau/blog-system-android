package top.manpok.blog.utils

import android.util.Log
import top.manpok.blog.BuildConfig
import top.manpok.blog.base.BaseApplication
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date


object LogUtil {

    private const val TAG = "LogUtil"

    private val logBuffer: MutableList<String> = mutableListOf()
    private const val BUFFER_SIZE: Int = 10
    private val logDateFormatter: SimpleDateFormat = SimpleDateFormat("MM-dd hh:mm:ss:SSS")
    private val fileDateFormatter: SimpleDateFormat = SimpleDateFormat("yyyyMMddhhmmssSSS")

    /**
     * 2MB分片
     */
    private const val LOG_FILE_SIZE: Long = 2097152
    private const val LOG_FILE_NUM = 10

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
        val date = logDateFormatter.format(Date())
        val logText = "$date $level $tag $msg\n"
        logBuffer.add(logText)
        if (logBuffer.size >= BUFFER_SIZE) {
            writeLog2File()
        }
    }

    @Synchronized
    private fun writeLog2File() {
        val rootPath = BaseApplication.getApplication().getExternalFilesDir("log")
        var file = File(rootPath, "log.txt")
        if (getFileSize(file) >= LOG_FILE_SIZE) {
            val formatFileTimeSuffix = fileDateFormatter.format(Date())
            val renameFile = File(rootPath, "log_${formatFileTimeSuffix}.txt")
            val renameSuccessfully = file.renameTo(renameFile)
            if (renameSuccessfully) {
                file = File(rootPath, "log.txt")
            }
        }
        val writer = FileWriter(file, true)
        val bufferedWriter = BufferedWriter(writer)
        try {
            logBuffer.forEach {
                bufferedWriter.write(it)
            }
            logBuffer.clear()
        } catch (e: Exception) {
            Log.e(TAG, "writeLog2File error: $e")
        } finally {
            try {
                bufferedWriter.close()
            } catch (e: Exception) {
                Log.e(TAG, "writeLog2File close error: $e")
            }
        }
        deleteRedundantLogFile()
    }

    @Synchronized
    private fun deleteRedundantLogFile() {
        val rootPath = BaseApplication.getApplication().getExternalFilesDir("log")
        val listFiles = rootPath?.listFiles()
        val fileNum = listFiles?.size
        if (fileNum != null && fileNum > LOG_FILE_NUM) {
            Arrays.sort(listFiles) { o1, o2 ->
                if (o1!!.lastModified() > o2!!.lastModified()) {
                    1
                } else if (o1.lastModified() == o2.lastModified()) {
                    0
                } else {
                    -1
                }
            }
            val deleteSuccessfully = listFiles[0].delete()
            i(TAG, "delete log file ${listFiles[0].name} successfully: $deleteSuccessfully")
        }
    }

    private fun getFileSize(file: File): Long {
        var fc: FileChannel? = null
        var fileSize: Long = 0
        try {
            if (file.exists() && file.isFile) {
                val fis = FileInputStream(file)
                fc = fis.channel
                fileSize = fc.size()
            } else {
                Log.e("getFileSize", "file doesn't exist or is not a file")
            }
        } catch (e: FileNotFoundException) {
            Log.e("getFileSize", e.message!!)
        } catch (e: IOException) {
            Log.e("getFileSize", e.message!!)
        } finally {
            if (null != fc) {
                try {
                    fc.close()
                } catch (e: IOException) {
                    Log.e("getFileSize", e.message!!)
                }
            }
        }
        return fileSize
    }

}