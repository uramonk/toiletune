package com.uramonk.toiletune.domain.repository

import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/25.
 */
interface DatabaseRepository {
    val onFetched: Observable<List<String>>
    fun fetchMediaList(): Observable<List<String>>
}