package com.uramonk.toiletune.domain.usecase

import com.uramonk.toiletune.domain.model.MediaInfo
import com.uramonk.toiletune.domain.repository.DatabaseRepository
import com.uramonk.toiletune.domain.repository.MediaRepository
import com.uramonk.toiletune.domain.repository.StorageRepository
import io.reactivex.Observable
import timber.log.Timber
import java.io.File

/**
 * Created by uramonk on 2018/11/25.
 */
class DownloadMedia(
        private val localDir: String,
        private val databaseRepository: DatabaseRepository,
        private val storageRepository: StorageRepository,
        private val mediaRepository: MediaRepository
) : DefaultObservableUseCase<List<MediaInfo>>() {
    override val observable: Observable<List<MediaInfo>>
        get() = databaseRepository.onFetched
                .flatMap { checkDownloads(it) }

    override fun onNext(t: List<MediaInfo>) {
        Timber.d("Downloaded media list: %s", t)
        mediaRepository.mediaList = t
    }

    private fun checkDownloads(list: List<MediaInfo>): Observable<List<MediaInfo>> =
            Observable.fromIterable(list)
                    .concatMap { toLocalInfo(localDir, it) }
                    .toList()
                    .toObservable()
                    .filter { it != mediaRepository.mediaList }
                    .flatMap { storageRepository.downloads(list) }

    private fun toLocalInfo(localDir: String, mediaInfo: MediaInfo): Observable<MediaInfo> =
            Observable.just(MediaInfo(localDir + "/" + File(mediaInfo.path).name,
                    mediaInfo.weight))
}