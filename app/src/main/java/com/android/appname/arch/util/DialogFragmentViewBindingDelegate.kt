package com.android.appname.arch.util

import android.view.View
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.android.appname.arch.extensions.ensureMainThread
import com.android.appname.ui.base.BaseDialogFragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class DialogFragmentViewBindingDelegate<T : ViewBinding>(
    private val fragment: BaseDialogFragment,
    private val viewBinder: (View) -> T,
    private val disposeEvents: T.() -> Unit = {}
) : ReadOnlyProperty<BaseDialogFragment, T>, LifecycleObserver {

    private inline fun BaseDialogFragment.observeLifecycleOwnerThroughLifecycleCreation(crossinline viewOwner: LifecycleOwner.() -> Unit) {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                viewLifecycleOwnerLiveData.observe(
                    this@observeLifecycleOwnerThroughLifecycleCreation,
                    Observer { viewLifecycleOwner ->
                        viewLifecycleOwner.viewOwner()
                    }
                )
            }
        })
    }

    private var fragmentBinding: T? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeBinding() {
        fragmentBinding?.disposeEvents()
        fragmentBinding = null
    }

    init {
        fragment.observeLifecycleOwnerThroughLifecycleCreation {
            lifecycle.addObserver(this@DialogFragmentViewBindingDelegate)
        }
    }

    override fun getValue(thisRef: BaseDialogFragment, property: KProperty<*>): T {
        ensureMainThread()
        val binding = fragmentBinding
        if (binding != null) {
            return binding
        }
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Fragment views are destroyed.")
        }
        return viewBinder(thisRef.requireView()).also { fragmentBinding = it }
    }
}
