package com.uramonk.toiletune.data.repository

import android.media.MediaPlayer
import com.uramonk.toiletune.domain.repository.PlayerRepository

/**
 * Created by uramonk on 2018/07/14.
 */
class PlayerDataRepository(
        private val mediaPlayer: MediaPlayer
) : PlayerRepository {

    override fun start() {
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }

    override fun stop() {
        mediaPlayer.pause()
    }
}