package com.uramonk.toiletune

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

/**
 * Created by uramonk on 2018/07/14.
 */
class ToiletuneApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}