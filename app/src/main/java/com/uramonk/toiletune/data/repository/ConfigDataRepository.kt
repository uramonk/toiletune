package com.uramonk.toiletune.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.uramonk.toiletune.Constants
import com.uramonk.toiletune.domain.model.PlayTime
import com.uramonk.toiletune.domain.repository.ConfigRepository
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by uramonk on 2018/11/10.
 */
class ConfigDataRepository(
        private val remoteConfig: FirebaseRemoteConfig
) : ConfigRepository {
    override val lightSensorThreshold: Float
        get() = remoteConfig.getDouble(Constants.LIGHT_SENSOR_THRESHOLD_KEY).toFloat()

    override val playTime: PlayTime
        get() = PlayTime.from(remoteConfig.getString(Constants.PLAY_TIME))

    override fun fetchConfig(): Observable<Boolean> {
        val isUsingDeveloperMode = remoteConfig.info.configSettings.isDeveloperModeEnabled
        val cacheExpiration: Long = if (isUsingDeveloperMode) {
            0
        } else {
            3600 // 1 hour in seconds.
        }

        Timber.d("(Current) light sensor threshold: %s", lightSensorThreshold)
        Timber.d("(Current) play time: %s", playTime)

        return Observable.create { emitter ->
            remoteConfig.fetch(cacheExpiration)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            remoteConfig.activateFetched()
                        }

                        Timber.d("(Fetched) light sensor threshold: %s", lightSensorThreshold)
                        Timber.d("(Fetched) play time: %s", playTime)

                        emitter.onNext(it.isSuccessful)
                        emitter.onComplete()
                    }
        }
    }
}