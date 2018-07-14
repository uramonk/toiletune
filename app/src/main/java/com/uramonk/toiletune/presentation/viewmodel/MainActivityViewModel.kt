package com.uramonk.toiletune.presentation.viewmodel

import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.view.View
import android.view.WindowManager
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.uramonk.toiletune.data.repository.LightSensorDataRepository
import com.uramonk.toiletune.data.repository.MediaDataRepository
import com.uramonk.toiletune.data.repository.PlayerDataRepository
import com.uramonk.toiletune.domain.repository.LightSensorRepository
import com.uramonk.toiletune.domain.repository.MediaRepository
import com.uramonk.toiletune.domain.repository.PlayerRepository
import com.uramonk.toiletune.domain.usecase.PlayMedia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by uramonk on 2018/07/14.
 */
class MainActivityViewModel(
        private val activity: RxActivity
) : BaseViewModel(activity) {

    /**
     * Repository
     */
    private lateinit var lightSensorRepository: LightSensorRepository
    private lateinit var playerRepository: PlayerRepository
    private lateinit var mediaRepository: MediaRepository

    /**
     * UseCase
     */
    private lateinit var playMedia: PlayMedia

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

        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private fun createRepository() {
        // Media
        val mediaList = listOf(
                Pair(activity.resources.getIdentifier("default_bgm", "raw",
                        activity.packageName), 49),
                Pair(activity.resources.getIdentifier("second_bgm", "raw",
                        activity.packageName), 1)
        )
        mediaRepository = MediaDataRepository(mediaList)

        // Player
        playerRepository = PlayerDataRepository(activity)

        // Sensor
        lightSensorRepository = LightSensorDataRepository(
                activity.getSystemService(SENSOR_SERVICE) as SensorManager)
        lightSensorRepository.start()

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.window
                .decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun createUseCase() {
        playMedia = PlayMedia(lightSensorRepository, playerRepository, mediaRepository)
    }

    private fun executeUseCase() {
        playMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
    }
}