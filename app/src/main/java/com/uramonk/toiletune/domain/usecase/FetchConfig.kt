package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.repository.ConfigRepository
import com.uramonk.toiletune.domain.repository.NotificationRepository
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/10.
 */
class FetchConfig(
        private val configRepository: ConfigRepository,
        private val notificationRepository: NotificationRepository
) : DefaultObservableUseCase<Boolean>() {
    override val observable: Observable<Boolean>
        get() = Observable.merge(configRepository.fetchConfig(),
                notificationRepository.onNotified.flatMap
                { configRepository.fetchConfig() })

    override fun onNext(t: Boolean) {
        // do nothing.
    }
}