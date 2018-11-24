package com.uramonk.toiletune.presentation.view

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.trello.rxlifecycle2.components.RxActivity
import com.uramonk.toiletune.Constants
import com.uramonk.toiletune.R
import com.uramonk.toiletune.databinding.ActivityMainBinding
import com.uramonk.toiletune.presentation.viewmodel.MainActivityViewModel
import timber.log.Timber


class MainActivity : RxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
                R.layout.activity_main)
        val mainActivityViewModel = MainActivityViewModel(this)
        binding.mainActivityViewModel = mainActivityViewModel

        // Call after ViewModel was created
        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    Timber.d("Sign out.")
                }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                response?.let {
                    Timber.d("Sign in failed.")
                    Timber.d(it.error)
                } ?: run {
                    Timber.d("Sign in failed because the user canceled.")
                }

            }
        }
    }
}
