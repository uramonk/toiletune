package com.uramonk.toiletune.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Created by uramonk on 2018/11/18.
 */
data class PlayTime(
        @Json(name = "start_time") val startTime: Time,
        @Json(name = "stop_time") val stopTime: Time
) {
    companion object {
        fun from(json: String): PlayTime {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(PlayTime::class.java)
            return adapter.fromJson(json)!!
        }
    }
}

data class Time(
        val hour: Int,
        val minute: Int
)
