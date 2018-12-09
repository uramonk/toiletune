package com.uramonk.toiletune.data.repository

import android.content.Context
import com.google.firebase.storage.FirebaseStorage
import com.uramonk.toiletune.domain.repository.StorageRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.File


/**
 * Created by uramonk on 2018/11/25.
 */
class FireStorageDataRepository(
        private val storage: FirebaseStorage,
        private val context: Context
) : StorageRepository {
    private val subject = PublishSubject.create<List<String>>()
    override val onDownloaded: Observable<List<String>>
        get() = subject.hide().share()

    override fun downloads(list: List<String>): Observable<List<String>> {
        return Observable.fromIterable(list)
                .concatMap { download(it) }
                .toList()
                .toObservable()
                .doOnNext { subject.onNext(it) }
                .doOnComplete { subject.onComplete() }
    }

    override fun download(path: String): Observable<String> {
        return Observable.create<String> { emitter ->
            val gsReference = storage.getReferenceFromUrl(path)
            val file = File(context.filesDir.path + "/" + File(path).name)
            Timber.d(file.absolutePath)
            gsReference.getFile(file).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Download completed.")
                    emitter.onNext(file.absolutePath)
                    emitter.onComplete()
                } else {
                    Timber.e(task.exception, "Download failed.")
                    task.exception?.let {
                        emitter.onError(it)
                    } ?: run {
                        emitter.onError(Exception("Unknown error."))
                    }
                }
            }.addOnProgressListener {
                Timber.d("Download progress: %s/%s.", it.bytesTransferred, it.totalByteCount)
            }
        }
    }
}