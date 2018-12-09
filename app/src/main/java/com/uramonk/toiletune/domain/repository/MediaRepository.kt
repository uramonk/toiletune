package com.uramonk.toiletune.domain.repository

import com.uramonk.toiletune.domain.model.MediaInfo

/**
 * Created by uramonk on 2018/07/14.
 */
interface MediaRepository {
    val size: Int
    val randomWeightSum: Int
    val mediaSourceWeightList: List<Int>
    var mediaList: List<MediaInfo>

    fun getMediaResourceWeight(index: Int): Int
    fun getMediaResource(index: Int): String
}