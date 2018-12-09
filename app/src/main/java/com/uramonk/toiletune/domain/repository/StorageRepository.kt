package com.uramonk.toiletune.domain.repository

import com.uramonk.toiletune.domain.model.MediaInfo
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/25.
 */
interface StorageRepository {
    val onDownloaded: Observable<List<MediaInfo>>
    fun downloads(list: List<MediaInfo>): Observable<List<MediaInfo>>
    fun download(path: MediaInfo): Observable<MediaInfo>
}