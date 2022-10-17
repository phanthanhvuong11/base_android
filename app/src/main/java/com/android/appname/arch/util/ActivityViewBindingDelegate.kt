package com.android.appname.arch.util

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.android.appname.arch.extensions.ensureMainThread
import com.android.appname.ui.base.BaseActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<T : ViewBinding>(
    private val activity: BaseActivity,
    private val viewBinder: (LayoutInflater) -> T,
    private val beforeSetContent: () -> Unit = {}
) : ReadOnlyProperty<BaseActivity, T>, LifecycleObserver {

    private var activityBinding: T? = null

    init {
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun createBinding() {
        initialize()
        beforeSetContent()
        activity.setContentView(activityBinding?.root)
        activity.lifecycle.removeObserver(this)
        activity.afterViewCreated()
    }

    private fun initialize() {
        if (activityBinding == null) {
            activityBinding = viewBinder(activity.layoutInflater)
        }
    }

    override fun getValue(thisRef: BaseActivity, property: KProperty<*>): T {
        ensureMainThread()

        initialize()
        return activityBinding!!
    }
}
