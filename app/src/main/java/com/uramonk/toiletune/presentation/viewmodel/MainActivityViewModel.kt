package com.uramonk.toiletune.presentation.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.SENSOR_SERVICE
import android.content.IntentFilter
import android.hardware.SensorManager
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.uramonk.toiletune.BuildConfig
import com.uramonk.toiletune.R
import com.uramonk.toiletune.UpdateReceiver
import com.uramonk.toiletune.data.repository.ConfigDataRepository
import com.uramonk.toiletune.data.repository.LightSensorDataRepository
import com.uramonk.toiletune.data.repository.MediaDataRepository
import com.uramonk.toiletune.data.repository.PlayerDataRepository
import com.uramonk.toiletune.domain.repository.*
import com.uramonk.toiletune.domain.usecase.FetchConfig
import com.uramonk.toiletune.domain.usecase.PlayMedia
import com.uramonk.toiletune.domain.usecase.StopMedia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by uramonk on 2018/07/14.
 */
class MainActivityViewModel(
        private val activity: RxActivity
) : BaseViewModel(activity) {
    /**
     * Firebase
     */
    private lateinit var remoteConfig: FirebaseRemoteConfig

    /**
     * Repository
     */
    private lateinit var lightSensorRepository: LightSensorRepository
    private lateinit var playerRepository: PlayerRepository
    private lateinit var mediaRepository: MediaRepository
    private lateinit var configRepository: ConfigRepository
    private lateinit var notificationRepository: NotificationRepository

    /**
     * UseCase
     */
    private lateinit var fetchConfig: FetchConfig
    private lateinit var playMedia: PlayMedia
    private lateinit var stopMedia: StopMedia

    override fun onCreate() {
        super.onCreate()
        initFirebase()
    }

    override fun onStart() {
        super.onStart()

        createReceiver()
        createRepository()
        createUseCase()
        executeUseCase()
    }

    override fun onStop() {
        super.onStop()

        lightSensorRepository.stop()
        playerRepository.stop()
        activity.unregisterReceiver(notificationRepository as UpdateReceiver)

        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun initFirebase() {
        // Remote Config
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.remote_config_defaults)

        // Cloud Messaging
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = activity.getString(R.string.default_notification_channel_id)
            val channelName = activity.getString(R.string.default_notification_channel_name)
            val notificationManager = activity.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }
    }

    private fun createReceiver() {
        notificationRepository = UpdateReceiver()
        val filter = IntentFilter()
        filter.addAction("PUSH_RC_ACTION")
        activity.registerReceiver(notificationRepository as UpdateReceiver, filter)
    }

    private fun createRepository() {
        // config
        configRepository = ConfigDataRepository(remoteConfig)

        // Media
        val mediaList = listOf(
                Pair(activity.resources.getIdentifier("a", "raw",
                        activity.packageName), 50),
                Pair(activity.resources.getIdentifier("b", "raw",
                        activity.packageName), 2),
                Pair(activity.resources.getIdentifier("c", "raw",
                        activity.packageName), 20),
                Pair(activity.resources.getIdentifier("d", "raw",
                        activity.packageName), 90),
                Pair(activity.resources.getIdentifier("e", "raw",
                        activity.packageName), 40),
                Pair(activity.resources.getIdentifier("f", "raw",
                        activity.packageName), 20)
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
        fetchConfig = FetchConfig(configRepository, notificationRepository)
        playMedia = PlayMedia(lightSensorRepository, playerRepository, mediaRepository,
                configRepository)
        stopMedia = StopMedia(lightSensorRepository, playerRepository, configRepository)
    }

    private fun executeUseCase() {
        fetchConfig.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
        playMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
        stopMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
    }
}