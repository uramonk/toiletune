package com.uramonk.toiletune.data.repository

import com.uramonk.toiletune.domain.repository.MediaRepository
import java.util.*

/**
 * Created by uramonk on 2018/07/14.
 */
class MediaDataRepository(
        private val mediaList: List<Pair<Int, Int>>
) : MediaRepository {
    override val size: Int
        get() = mediaList.size

    override fun getMediaResource(index: Int): Int {
        return mediaList[index].first
    }

    override fun getRandomMediaReource(): Int {
        val max = mediaList.sumBy {
            it.second
        }
        val randomIndex = Random().nextInt(max) + 1
        var sum = 0
        mediaList.forEachIndexed { index, pair ->
            if (randomIndex > sum && randomIndex <= sum + pair.second) {
                return getMediaResource(index)
            }
            sum += pair.second
        }
        return getMediaResource(0)
    }
}