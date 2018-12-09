package com.uramonk.toiletune.domain.repository

import com.uramonk.toiletune.domain.model.MediaInfo
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/25.
 */
interface DatabaseRepository {
    val onFetched: Observable<List<MediaInfo>>
    fun fetchMediaList(): Observable<List<MediaInfo>>
}