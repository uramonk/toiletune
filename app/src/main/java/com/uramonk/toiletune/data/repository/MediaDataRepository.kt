package com.uramonk.toiletune.data.repository

import com.uramonk.toiletune.domain.model.MediaInfo
import com.uramonk.toiletune.domain.repository.MediaRepository

/**
 * Created by uramonk on 2018/07/14.
 */
class MediaDataRepository(

) : MediaRepository {
    override var mediaList: List<MediaInfo> = listOf()

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