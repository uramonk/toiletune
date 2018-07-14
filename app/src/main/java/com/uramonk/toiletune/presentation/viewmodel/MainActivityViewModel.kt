package com.uramonk.toiletune.presentation.viewmodel

import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.media.MediaPlayer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uramonk.toiletune.data.repository.LightSensorDataRepository
import com.uramonk.toiletune.data.repository.PlayerDataRepository
import com.uramonk.toiletune.domain.repository.LightSensorRepository
import com.uramonk.toiletune.domain.repository.PlayerRepository
import com.uramonk.toiletune.domain.usecase.PlayMedia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by uramonk on 2018/07/14.
 */
class MainActivityViewModel(
        private val activity: RxAppCompatActivity
) : BaseViewModel(activity) {

    /**
     * Repository
     */
    private lateinit var lightSensorRepository: LightSensorRepository
    private lateinit var playerRepository: PlayerRepository

    /**
     * UseCase
     */
    private lateinit var playMedia: PlayMedia

    /**
     * Other
     */
    private lateinit var mediaPlayer: MediaPlayer

    override fun onStart() {
        super.onStart()

        createRepository()
        createUseCase()
        executeUseCase()
    }

    override fun onStop() {
        super.onStop()

        lightSensorRepository.stop()
        playerRepository.stop()
    }

    private fun createRepository() {
        // Player
        val mediaResource = activity.resources.getIdentifier("healing2", "raw",
                activity.packageName)
        mediaPlayer = MediaPlayer.create(activity, mediaResource)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(1f, 1f)
        playerRepository = PlayerDataRepository(mediaPlayer)

        // Sensor
        lightSensorRepository = LightSensorDataRepository(
                activity.getSystemService(SENSOR_SERVICE) as SensorManager)
        lightSensorRepository.start()
    }

    private fun createUseCase() {
        playMedia = PlayMedia(lightSensorRepository, playerRepository)
    }

    private fun executeUseCase() {
        playMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
    }
}