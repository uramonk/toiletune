package com.uramonk.toiletune.domain.usecase

import io.reactivex.disposables.Disposable

/**
 * Created by uramonk on 2018/07/14.
 */
abstract class DefaultObservableUseCase<T>: ObservableUseCase<T>() {

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }
}