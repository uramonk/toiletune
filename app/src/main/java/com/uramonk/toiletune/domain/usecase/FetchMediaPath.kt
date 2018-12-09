package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.model.MediaInfo
import com.uramonk.toiletune.domain.repository.DatabaseRepository
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by uramonk on 2018/11/25.
 */
class FetchMediaPath(
        private val databaseRepository: DatabaseRepository
) : DefaultObservableUseCase<List<MediaInfo>>() {
    override val observable: Observable<List<MediaInfo>>
        get() = databaseRepository.fetchMediaList()

    override fun onNext(t: List<MediaInfo>) {
        Timber.d("Fetched media list: %s", t)
    }
}