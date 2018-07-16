package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.repository.LightSensorRepository
import com.uramonk.toiletune.domain.repository.MediaRepository
import com.uramonk.toiletune.domain.repository.PlayerRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.threeten.bp.LocalTime
import java.util.*

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
                .map { Pair(it, LocalTime.now()) }
                .filter {
                    it.second.isAfter(LocalTime.of(8, 0)) && it.second.isBefore(
                            LocalTime.of(23, 0))
                }
                .map { it.first > 10f }
                .distinctUntilChanged()

    override fun onNext(t: Boolean) {
        if (t) {
            playerRepository.setDataSource(getRandomMediaResource())
            playerRepository.start()
        } else {
            playerRepository.stop()
        }
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