package com.uramonk.toiletune.domain.repository

import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/10.
 */
interface ConfigRepository {
    val lightSensorThreshold: Float
    fun fetchLightSensorThreshold(): Observable<Float>
}