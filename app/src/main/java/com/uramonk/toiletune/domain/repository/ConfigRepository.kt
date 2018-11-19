package com.uramonk.toiletune.domain.repository

import com.uramonk.toiletune.domain.model.PlayTime
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/10.
 */
interface ConfigRepository {
    val lightSensorThreshold: Float
    val playTIme: PlayTime
    fun fetchConfig(): Observable<Boolean>
}