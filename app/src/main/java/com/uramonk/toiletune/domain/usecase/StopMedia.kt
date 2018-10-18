package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.Constants
import com.uramonk.toiletune.domain.repository.LightSensorRepository
import com.uramonk.toiletune.domain.repository.PlayerRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

/**
 * Created by uramonk on 2018/07/30.
 *
 * 暗くなった時に音楽を停止するUseCase.
 */
class StopMedia(
        private val sensorRepository: LightSensorRepository,
        private val playerRepository: PlayerRepository
) : DefaultObservableUseCase<Unit>() {
    override val observable: Observable<Unit>
        get() = sensorRepository.onSensorChanged
                .toFlowable(BackpressureStrategy.DROP)
                .toObservable()
                // 明るい状態（しきい値を超える）から暗い状態（しきい値以下）になった場合のみ通過する。
                .map { it <= Constants.LIGHT_SENSOR_THRESHOLD }
                .distinctUntilChanged()
                .filter { it }
                .map { Unit }

    override fun onNext(t: Unit) {
        // 音楽を停止する。
        playerRepository.stop()
    }
}