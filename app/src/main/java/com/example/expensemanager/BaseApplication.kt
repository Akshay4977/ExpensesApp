package com.example.expensemanager

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null
        fun applicationContext(): Context {
            Log.e("inside","BaseApplication")
            return instance!!.applicationContext
        }
    }
}
