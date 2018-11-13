package com.uramonk.toiletune

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.uramonk.toiletune.domain.repository.NotificationRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by uramonk on 2018/11/13.
 */
class UpdateReceiver : BroadcastReceiver(), NotificationRepository {
    private val subject: PublishSubject<Unit> = PublishSubject.create()
    override val onNotified: Observable<Unit> = subject.hide().share()

    override fun onReceive(context: Context?, intent: Intent?) {
        subject.onNext(Unit)
    }
}