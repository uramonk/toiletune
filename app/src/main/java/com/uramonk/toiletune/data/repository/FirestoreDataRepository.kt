package com.uramonk.toiletune.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.uramonk.toiletune.domain.model.MediaInfo
import com.uramonk.toiletune.domain.repository.DatabaseRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by uramonk on 2018/11/25.
 */
class FirestoreDataRepository(
        private val firestore: FirebaseFirestore
) : DatabaseRepository {
    private val subject = PublishSubject.create<List<MediaInfo>>()
    override val onFetched: Observable<List<MediaInfo>>
        get() = subject.hide().share()

    override fun fetchMediaList(): Observable<List<MediaInfo>> {
        return Observable.create<List<MediaInfo>> { emitter ->
            val docRef = firestore.collection("media").document("music")
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    document?.let { it ->
                        if (it.exists()) {
                            Timber.d("DocumentSnapshot data: %s", document.data)
                            document.data?.let { data ->
                                val list = (data["list"] as List<*>).map { it -> it as Map<*, *> }
                                        .map {
                                            MediaInfo(it["file"] as String,
                                                    (it["weight"] as Long).toInt())
                                        }.toList()
                                emitter.onNext(list)
                                subject.onNext(list)
                            }
                        } else {
                            Timber.d("No such document")
                            emitter.onNext(listOf())
                            subject.onNext(listOf())
                        }
                        emitter.onComplete()
                        subject.onComplete()
                    }

                } else {
                    Timber.e(task.exception, "get failed.")
                    task.exception?.let {
                        emitter.onError(it)
                    } ?: run {
                        emitter.onError(Exception("Unknown error."))
                    }

                }
            }
        }
    }
}