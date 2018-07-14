package com.uramonk.toiletune.domain.repository

/**
 * Created by uramonk on 2018/07/14.
 */
interface MediaRepository {
    val size: Int
    fun getMediaResource(index: Int): Int
    fun getRandomMediaReource(): Int
}