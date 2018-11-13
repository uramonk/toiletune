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
) : DefaultObservableUseCase<Float>() {
    override val observable: Observable<Float>
        get() = Observable.merge(configRepository.fetchLightSensorThreshold(),
                notificationRepository.onNotified.flatMap
                { configRepository.fetchLightSensorThreshold() })

    override fun onNext(t: Float) {
        // do nothing.
    }
}