package com.uramonk.toiletune.data.repository

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.uramonk.toiletune.domain.repository.LightSensorRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by uramonk on 2018/07/14.
 */
class LightSensorDataRepository(
        private val sensorManager: SensorManager
): LightSensorRepository, SensorEventListener {

    private val sensorChangedSubject = PublishSubject.create<Float>()
    override val onSensorChanged: Observable<Float>
        get() = sensorChangedSubject.hide().share()

    override fun start() {
        val sensors = sensorManager.getSensorList(Sensor.TYPE_LIGHT)
        if(sensors.size > 0) {
            val sensor = sensors.get(0)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_LIGHT) {
            sensorChangedSubject.onNext(event.values[0])
        }
    }
}