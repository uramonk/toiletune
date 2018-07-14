package com.uramonk.toiletune.domain.repository

import io.reactivex.Observable

/**
 * Created by uramonk on 2018/07/14.
 */
interface LightSensorRepository {
    val onSensorChanged: Observable<Float>

    fun start()
    fun stop()
}