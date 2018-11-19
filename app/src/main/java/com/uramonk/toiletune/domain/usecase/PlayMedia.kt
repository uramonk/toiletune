package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.repository.ConfigRepository
import com.uramonk.toiletune.domain.repository.LightSensorRepository
import com.uramonk.toiletune.domain.repository.MediaRepository
import com.uramonk.toiletune.domain.repository.PlayerRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.threeten.bp.LocalTime
import java.util.*

/**
 * Created by uramonk on 2018/07/14.
 *
 * 特定時間帯に明るくなった時に音楽を再生するUseCase.
 */
class PlayMedia(
        private val sensorRepository: LightSensorRepository,
        private val playerRepository: PlayerRepository,
        private val mediaRepository: MediaRepository,
        private val configRepository: ConfigRepository
) : DefaultObservableUseCase<Unit>() {
    override val observable: Observable<Unit>
        get() = sensorRepository.onSensorChanged
                .toFlowable(BackpressureStrategy.DROP)
                .toObservable()
                // 暗い状態（しきい値以下）から明るい状態（しきい値を超える）になった場合のみ通過する。
                .map { it > configRepository.lightSensorThreshold }
                .distinctUntilChanged()
                .filter { it }
                // 特定時間帯のみ通過する。
                .map { LocalTime.now() }
                .filter {
                    it.isAfter(configRepository.playTime.startTime) && it.isBefore(
                            configRepository.playTime.stopTime)
                }
                .map { Unit }

    override fun onNext(t: Unit) {
        // ランダムに音楽を取得して再生する。
        playerRepository.setDataSource(getRandomMediaResource())
        playerRepository.start()
    }

    private fun getRandomMediaResource(): Int {
        val max = mediaRepository.randomWeightSum
        val randomIndex = Random().nextInt(max) + 1
        var sum = 0
        mediaRepository.mediaSourceWeightList.forEachIndexed { index, i ->
            if (randomIndex > sum && randomIndex <= sum + i) {
                return mediaRepository.getMediaResource(index)
            }
            sum += i
        }
        return mediaRepository.getMediaResource(0)
    }
}