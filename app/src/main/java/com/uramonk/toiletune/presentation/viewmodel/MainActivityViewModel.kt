package com.uramonk.toiletune.presentation.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.IntentFilter
import android.content.SharedPreferences
import android.hardware.SensorManager
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.uramonk.toiletune.BuildConfig
import com.uramonk.toiletune.R
import com.uramonk.toiletune.UpdateReceiver
import com.uramonk.toiletune.data.repository.*
import com.uramonk.toiletune.domain.repository.*
import com.uramonk.toiletune.domain.usecase.*
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
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    /**
     * Repository
     */
    private lateinit var lightSensorRepository: LightSensorRepository
    private lateinit var playerRepository: PlayerRepository
    private lateinit var mediaRepository: MediaRepository
    private lateinit var configRepository: ConfigRepository
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var databaseRepository: DatabaseRepository
    private lateinit var storageRepository: StorageRepository

    /**
     * UseCase
     */
    private lateinit var fetchConfig: FetchConfig
    private lateinit var playMedia: PlayMedia
    private lateinit var stopMedia: StopMedia
    private lateinit var fetchMediaPath: FetchMediaPath
    private lateinit var downloadMedia: DownloadMedia

    private lateinit var preferences: SharedPreferences

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

        authRepository.showLogin()
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

        // FireStore
        firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        firestore.firestoreSettings = settings

        // Storage
        storage = FirebaseStorage.getInstance()
    }

    private fun createReceiver() {
        notificationRepository = UpdateReceiver()
        val filter = IntentFilter()
        filter.addAction("PUSH_RC_ACTION")
        activity.registerReceiver(notificationRepository as UpdateReceiver, filter)
    }

    private fun createRepository() {
        preferences = activity.getSharedPreferences("toiletune_data", Context.MODE_PRIVATE)

        // config
        configRepository = ConfigDataRepository(remoteConfig)

        // Media
        mediaRepository = MediaDataRepository(preferences)

        // Player
        playerRepository = PlayerDataRepository(activity)

        // Sensor
        lightSensorRepository = LightSensorDataRepository(
                activity.getSystemService(SENSOR_SERVICE) as SensorManager)
        lightSensorRepository.start()

        authRepository = AuthDataRepository(activity)
        databaseRepository = FirestoreDataRepository(firestore)
        storageRepository = FireStorageDataRepository(storage, activity)

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
        fetchMediaPath = FetchMediaPath(databaseRepository)
        downloadMedia = DownloadMedia(activity.filesDir.path, databaseRepository, storageRepository,
                mediaRepository)
    }

    private fun executeUseCase() {
        fetchConfig.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
        playMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
        stopMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
        fetchMediaPath.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
        downloadMedia.execute(Schedulers.newThread(), AndroidSchedulers.mainThread(),
                activity.bindUntilEvent(ActivityEvent.STOP))
    }
}