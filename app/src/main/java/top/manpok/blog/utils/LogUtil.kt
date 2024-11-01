package top.manpok.blog.utils

import android.os.Process
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import top.manpok.blog.BuildConfig
import top.manpok.blog.R
import top.manpok.blog.base.BaseApplication
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


object LogUtil {

    private const val TAG = "LogUtil"

    private val logBuffer: MutableList<String> = mutableListOf()
    private const val BUFFER_SIZE: Int = 10
    private val logDateFormatter: SimpleDateFormat = SimpleDateFormat("MM-dd hh:mm:ss:SSS")
    val fileDateFormatter: SimpleDateFormat = SimpleDateFormat("yyyyMMddhhmmssSSS")
    private val mutex: Mutex = Mutex()

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
        GlobalScope.launch(Dispatchers.IO) {
            mutex.withLock {
                add2LogBuffer(tag, msg, "I")
            }
        }
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg)
        }
        GlobalScope.launch(Dispatchers.IO) {
            mutex.withLock {
                add2LogBuffer(tag, msg, "W")
            }
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
        GlobalScope.launch(Dispatchers.IO) {
            mutex.withLock {
                add2LogBuffer(tag, msg, "E")
            }
        }
    }

    private suspend fun add2LogBuffer(tag: String, msg: String, level: String) {
        val date = logDateFormatter.format(Date())
        val logText: String
        if (msg.length > Constants.LOG_MSG_MAX_LENGTH) {
            val simpleMsg = msg.substring(0, Constants.LOG_MSG_MAX_LENGTH)
            logText = "$date $level ${Process.myPid()}-${Process.myTid()} $tag $simpleMsg\n"
        } else {
            logText = "$date $level ${Process.myPid()}-${Process.myTid()} $tag $msg\n"
        }
        logBuffer.add(logText)
        if (logBuffer.size >= BUFFER_SIZE) {
            writeLog2File()
        }
    }

    suspend fun flushLogBuffer() {
        mutex.withLock {
            writeLog2File()
        }
    }

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

    fun writeCrashLog2File(msg: String) {
        val rootPath = BaseApplication.getApplication().getExternalFilesDir("log")
        var file = File(rootPath, "crash_log.txt")
        if (getFileSize(file) >= LOG_FILE_SIZE) {
            val formatFileTimeSuffix = fileDateFormatter.format(Date())
            val renameFile = File(rootPath, "crash_log_${formatFileTimeSuffix}.txt")
            val renameSuccessfully = file.renameTo(renameFile)
            if (renameSuccessfully) {
                file = File(rootPath, "crash_log.txt")
            }
        }
        val writer = FileWriter(file, true)
        val bufferedWriter = BufferedWriter(writer)
        try {
            bufferedWriter.write(msg)
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

    suspend fun createZipFile() {
        val rootPath = BaseApplication.getApplication().getExternalFilesDir("log")
        if (rootPath == null || !rootPath.exists()) {
            ToastUtil.showShortToast(R.string.empty_log_files)
            return
        }
        var listFiles = rootPath.listFiles()
        if (listFiles == null || listFiles.isEmpty()) {
            ToastUtil.showShortToast(R.string.empty_log_files)
            return
        }
        val zipFile = File(rootPath, "log.zip")
        if (zipFile.exists()) {
            zipFile.delete()
            listFiles = rootPath.listFiles()
        }
        val fileOutputStream = FileOutputStream(zipFile)
        val zipOutputStream = ZipOutputStream(fileOutputStream)
        try {
            listFiles.forEach {
                val zipEntry = ZipEntry(it.name)
                zipOutputStream.putNextEntry(zipEntry)
                val fileInputStream = FileInputStream(it)
                try {
                    val buffer = ByteArray(4096)
                    var len = fileInputStream.read(buffer)
                    while (len > 0) {
                        zipOutputStream.write(buffer, 0, len)
                        len = fileInputStream.read(buffer)
                    }
                } catch (e: Exception) {
                    e(TAG, "output zip file error1: $e")
                } finally {
                    try {
                        zipOutputStream.closeEntry()
                        fileInputStream.close()
                    } catch (e: Exception) {
                        e(TAG, "close stream error1: $e")
                    }
                }
            }
        } catch (e: Exception) {
            e(TAG, "output zip file error2: $e")
        } finally {
            try {
                zipOutputStream.close()
                fileOutputStream.close()
            } catch (e: Exception) {
                e(TAG, "close stream error2: $e")
            }
        }
    }

}