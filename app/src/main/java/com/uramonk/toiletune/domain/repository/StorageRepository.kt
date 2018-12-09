package com.uramonk.toiletune.domain.repository

import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/25.
 */
interface StorageRepository {
    val onDownloaded: Observable<List<String>>
    fun downloads(list: List<String>): Observable<List<String>>
    fun download(path: String): Observable<String>
}