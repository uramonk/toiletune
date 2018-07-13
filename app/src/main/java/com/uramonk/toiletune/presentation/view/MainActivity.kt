package com.uramonk.toiletune.presentation.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uramonk.toiletune.R
import com.uramonk.toiletune.databinding.ActivityMainBinding
import com.uramonk.toiletune.presentation.viewmodel.MainActivityViewModel

class MainActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
                R.layout.activity_main)
        val mainActivityViewModel = MainActivityViewModel(this)
        binding.mainActivityViewModel = mainActivityViewModel

        // Call after ViewModel was created
        super.onCreate(savedInstanceState)
    }
}
