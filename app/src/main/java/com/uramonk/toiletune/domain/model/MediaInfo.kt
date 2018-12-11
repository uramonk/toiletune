package com.uramonk.toiletune.domain.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Created by uramonk on 2018/12/09.
 */
data class MediaInfo(
        val path: String,
        val weight: Int
) {
    companion object {
        fun from(json: String): MediaInfo {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val adapter = moshi.adapter(MediaInfo::class.java)
            return adapter.fromJson(json)!!
        }

        fun fromList(json: String): List<MediaInfo> {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val type = Types.newParameterizedType(List::class.java, MediaInfo::class.java)
            val adapter = moshi.adapter<List<MediaInfo>>(type)
            return adapter.fromJson(json)!!
        }

        fun to(mediaInfo: MediaInfo): String {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val adapter = moshi.adapter(MediaInfo::class.java)
            return adapter.toJson(mediaInfo)
        }

        fun toList(list: List<MediaInfo>): String {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val type = Types.newParameterizedType(List::class.java, MediaInfo::class.java)
            val adapter = moshi.adapter<List<MediaInfo>>(type)
            return adapter.toJson(list)
        }
    }
}