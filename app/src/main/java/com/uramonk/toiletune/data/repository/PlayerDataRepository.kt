package com.uramonk.toiletune.data.repository

import android.content.Context
import android.media.MediaPlayer
import com.uramonk.toiletune.domain.repository.PlayerRepository

/**
 * Created by uramonk on 2018/07/14.
 */
class PlayerDataRepository(
        private val context: Context
) : PlayerRepository {
    private var mediaPlayer: MediaPlayer? = null

    override fun start() {
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
    }

    override fun stop() {
        mediaPlayer?.pause()
    }

    override fun setDataSource(dataSource: Int) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer = MediaPlayer.create(context, dataSource)
        setSetting()
    }

    private fun setSetting() {
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(1f, 1f)
    }
}