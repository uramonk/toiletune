package com.uramonk.toiletune.presentation.viewmodel

import android.app.Activity
import android.app.Fragment
import android.support.annotation.CallSuper
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by uramonk on 2018/07/14.
 */
open class BaseViewModel {

    constructor(activity: RxActivity) {
        subscribeActivityLifecycle(activity, ActivityEvent.DESTROY)
    }

    constructor(activity: RxActivity, activityEvent: ActivityEvent) {
        subscribeActivityLifecycle(activity, activityEvent)
    }

    private fun subscribeActivityLifecycle(activity: RxActivity,
            activityEvent: ActivityEvent) {
        activity.lifecycle()
                .compose(activity.bindUntilEvent<ActivityEvent?>(activityEvent))
                .subscribe {
                    when (it) {
                        ActivityEvent.CREATE -> onCreate()
                        ActivityEvent.START -> onStart()
                        ActivityEvent.RESUME -> onResume()
                        ActivityEvent.PAUSE -> onPause()
                        ActivityEvent.STOP -> onStop()
                        ActivityEvent.DESTROY -> onDestroy()
                    }
                }
    }

    constructor(fragment: RxFragment) {
        subscribeFragmentLifecycle(fragment, FragmentEvent.DESTROY_VIEW)
    }

    constructor(fragment: RxFragment, fragmentEvent: FragmentEvent) {
        subscribeFragmentLifecycle(fragment, fragmentEvent)
    }

    private fun subscribeFragmentLifecycle(fragment: RxFragment,
            fragmentEvent: FragmentEvent) {
        fragment.lifecycle()
                .compose(fragment.bindUntilEvent<FragmentEvent?>(fragmentEvent))
                .subscribe {
                    when (it) {
                        FragmentEvent.CREATE -> onCreate()
                        FragmentEvent.CREATE_VIEW -> onCreateView()
                        FragmentEvent.ATTACH -> onAttach()
                        FragmentEvent.START -> onStart()
                        FragmentEvent.RESUME -> onResume()
                        FragmentEvent.PAUSE -> onPause()
                        FragmentEvent.STOP -> onStop()
                        FragmentEvent.DETACH -> onDetach()
                        FragmentEvent.DESTROY_VIEW -> onDestroyView()
                        FragmentEvent.DESTROY -> onDestroy()
                    }
                }
    }

    @CallSuper
    protected open fun onCreate() {
        // Implement common process
    }

    @CallSuper
    protected open fun onCreateView() {

    }

    @CallSuper
    protected open fun onAttach() {

    }

    @CallSuper
    protected open fun onStart() {

    }

    @CallSuper
    protected open fun onResume() {

    }

    @CallSuper
    protected open fun onPause() {

    }

    @CallSuper
    protected open fun onStop() {

    }

    @CallSuper
    protected open fun onDetach() {

    }

    @CallSuper
    protected open fun onDestroyView() {

    }

    @CallSuper
    protected open fun onDestroy() {

    }

    protected fun commitFragment(activity: Activity, fragment: Fragment, container_id: Int) {
        val transaction = activity.fragmentManager.beginTransaction()
        transaction.replace(container_id, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}