package com.uramonk.toiletune.domain.usecase

import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

/**
 * Created by uramonk on 2018/07/14.
 */
interface Executable<T> {
    fun execute(executionScheduler: Scheduler, postScheduler: Scheduler, lifecycleTransformer: LifecycleTransformer<T>): Disposable
}