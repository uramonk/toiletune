package com.uramonk.toiletune.data.repository

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.uramonk.toiletune.Constants
import com.uramonk.toiletune.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Created by uramonk on 2018/11/24.
 */
class AuthDataRepository(
        private val activity: Activity
) : AuthRepository {
    override fun showLogin() {
        FirebaseAuth.getInstance().currentUser?.let {
            Timber.d("Already sign in.")
        } ?: run {
            Timber.d("Not sign in.")
            val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build()
            )
            activity.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    Constants.RC_SIGN_IN
            )
        }
    }
}