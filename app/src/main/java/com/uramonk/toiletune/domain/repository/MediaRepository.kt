package com.uramonk.toiletune.domain.repository

/**
 * Created by uramonk on 2018/07/14.
 */
interface MediaRepository {
    val size: Int
    val randomWeightSum: Int
    val mediaSourceWeightList: List<Int>

    fun getMediaResourceWeight(index: Int): Int
    fun getMediaResource(index: Int): Int
}