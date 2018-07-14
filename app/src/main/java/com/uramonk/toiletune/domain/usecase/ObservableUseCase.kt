package com.uramonk.toiletune.domain.usecase

import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

/**
 * Created by uramonk on 2018/07/14.
 */
abstract class ObservableUseCase<T>: Executable<T>, Observer<T> {
    abstract val observable: Observable<T>
    private val observer: Observer<T>
        get() = this

    override fun execute(executionScheduler: Scheduler, postScheduler: Scheduler, lifecycleTransformer: LifecycleTransformer<T>): Disposable {
        return observable
                .subscribeOn(executionScheduler)
                .observeOn(postScheduler)
                .compose(lifecycleTransformer)
                .subscribe(observer::onNext, observer::onError, observer::onComplete, observer::onSubscribe)
    }
}