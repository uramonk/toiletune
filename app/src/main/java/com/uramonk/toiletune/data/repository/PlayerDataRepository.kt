package com.uramonk.toiletune.data.repository

import android.content.Context
import android.media.MediaPlayer
import com.uramonk.toiletune.domain.repository.PlayerRepository
import java.io.File
import java.io.FileInputStream
import java.io.IOException

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

    override fun setDataSource(dataSource: String) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer = MediaPlayer()

        var fis: FileInputStream? = null
        try {
            // 再生ファイルをセット
            fis = FileInputStream(File(dataSource))
            val fd = fis.fd
            mediaPlayer?.setDataSource(fd)
            mediaPlayer?.prepare() // 再生準備
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        setSetting()
    }

    private fun setSetting() {
        mediaPlayer?.isLooping = true
        mediaPlayer?.setVolume(1f, 1f)
    }
}