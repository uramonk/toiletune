package com.uramonk.toiletune.data.repository

import android.content.SharedPreferences
import com.uramonk.toiletune.domain.model.MediaInfo
import com.uramonk.toiletune.domain.repository.MediaRepository

/**
 * Created by uramonk on 2018/07/14.
 */
class MediaDataRepository(
        private val preferences: SharedPreferences
) : MediaRepository {
    companion object {
        private const val PREF_MEDIA_LIST = "media_list"
    }

    override var mediaList: List<MediaInfo>
        get() = preferences.getString(PREF_MEDIA_LIST, null)?.let {
            MediaInfo.fromList(it)
        } ?: listOf()
        set(value) {
            val editor = preferences.edit()
            editor.putString(PREF_MEDIA_LIST, MediaInfo.toList(value))
            editor.apply()
        }

    override val size: Int
        get() = mediaList.size

    override val randomWeightSum: Int
        get() = mediaList.sumBy {
            it.weight
        }

    override val mediaSourceWeightList: List<Int>
        get() = mediaList.map {
            it.weight
        }.toList()

    override fun getMediaResourceWeight(index: Int): Int {
        return mediaList[index].weight
    }

    override fun getMediaResource(index: Int): String {
        return mediaList[index].path
    }
}