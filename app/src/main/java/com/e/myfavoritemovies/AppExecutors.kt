package com.e.myfavoritemovies

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {

    private val diskIO: Executor = Executors.newSingleThreadExecutor()
    private val networkIO: Executor = Executors.newFixedThreadPool(3)
    private val mainThread: Executor = MainThreadExecutor()

    @JvmStatic
    fun getInstance(): AppExecutors = this

    fun diskIO(): Executor = diskIO

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
