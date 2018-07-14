package com.uramonk.toiletune.data.repository

import com.uramonk.toiletune.domain.repository.MediaRepository

/**
 * Created by uramonk on 2018/07/14.
 */
class MediaDataRepository(
        private val mediaList: List<Pair<Int, Int>>
) : MediaRepository {
    override val size: Int
        get() = mediaList.size

    override val randomWeightSum: Int
        get() = mediaList.sumBy {
            it.second
        }

    override val mediaSourceWeightList: List<Int>
        get() = mediaList.map {
            it.second
        }.toList()

    override fun getMediaResourceWeight(index: Int): Int {
        return mediaList[index].second
    }

    override fun getMediaResource(index: Int): Int {
        return mediaList[index].first
    }
}