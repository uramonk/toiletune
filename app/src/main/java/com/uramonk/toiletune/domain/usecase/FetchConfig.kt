package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.repository.ConfigRepository
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/11/10.
 */
class FetchConfig(
        private val configRepository: ConfigRepository
) : DefaultObservableUseCase<Float>() {
    override val observable: Observable<Float>
        get() = configRepository.fetchLightSensorThreshold()

    override fun onNext(t: Float) {
        // do nothing.
    }
}