package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.repository.LightSensorRepository
import com.uramonk.toiletune.domain.repository.MediaRepository
import com.uramonk.toiletune.domain.repository.PlayerRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/07/14.
 */
class PlayMedia(
        private val sensorRepository: LightSensorRepository,
        private val playerRepository: PlayerRepository,
        private val mediaRepository: MediaRepository
) : DefaultObservableUseCase<Boolean>() {
    override val observable: Observable<Boolean>
        get() = sensorRepository.onSensorChanged
                .toFlowable(BackpressureStrategy.DROP)
                .toObservable()
                .map { it > 10f }
                .distinctUntilChanged()

    override fun onNext(t: Boolean) {
        if (t) {
            playerRepository.setDataSource(mediaRepository.getRandomMediaReource())
            playerRepository.start()
        } else {
            playerRepository.stop()
        }
    }
}