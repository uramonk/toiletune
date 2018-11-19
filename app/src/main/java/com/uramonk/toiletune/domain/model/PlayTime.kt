package com.uramonk.toiletune.domain.model

import com.squareup.moshi.*
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.threeten.bp.LocalTime

/**
 * Created by uramonk on 2018/11/18.
 */
data class PlayTime(
        @Json(name = "start_time") val startTime: LocalTime,
        @Json(name = "stop_time") val stopTime: LocalTime
) {
    companion object {
        fun from(json: String): PlayTime {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(LocalTimeAdapter.FACTORY)
                    .build()
            val adapter = moshi.adapter(PlayTime::class.java)
            return adapter.fromJson(json)!!
        }
    }
}

class LocalTimeAdapter : JsonAdapter<LocalTime>() {
    companion object {
        val FACTORY: Factory = Factory { type, _, _ ->
            if (type === LocalTime::class.java) {
                return@Factory LocalTimeAdapter()
            }
            null
        }
    }


    override fun fromJson(reader: JsonReader): LocalTime? {
        var hour = 0
        var minute = 0
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            when (name) {
                "hour" -> hour = reader.nextInt()
                "minute" -> minute = reader.nextInt()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return LocalTime.of(hour, minute)
    }

    override fun toJson(writer: JsonWriter, value: LocalTime?) {
        value?.let {
            writer.beginObject()
                    .name("hour").value(it.hour)
                    .name("minute").value(it.minute)
                    .endObject()
        }
    }

    override fun toString(): String {
        return "JsonAdapter(LocalTime)"
    }
}