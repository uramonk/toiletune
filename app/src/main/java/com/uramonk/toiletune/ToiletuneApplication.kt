package com.uramonk.toiletune

import android.app.Application
import timber.log.Timber

/**
 * Created by uramonk on 2018/07/14.
 */
class ToiletuneApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}