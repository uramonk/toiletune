package com.uramonk.toiletune.domain.repository

import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/13.
 */
interface NotificationRepository {
    val onNotified: Observable<Unit>
}