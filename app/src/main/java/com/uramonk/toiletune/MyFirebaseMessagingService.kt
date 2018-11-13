package com.uramonk.toiletune

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


/**
 * Created by uramonk on 2018/11/10.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        Timber.d("FCM New Token: %s", s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val broadcast = Intent()
        broadcast.action = "PUSH_RC_ACTION"
        baseContext.sendBroadcast(broadcast)
    }
}