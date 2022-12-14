package com.android.appname.arch.extensions

import android.app.Activity
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.android.appname.arch.util.ActivityViewBindingDelegate
import com.android.appname.arch.util.DialogFragmentViewBindingDelegate
import com.android.appname.arch.util.FragmentViewBindingDelegate
import com.android.appname.arch.util.GlobalViewBindingDelegate
import com.android.appname.ui.base.BaseActivity
import com.android.appname.ui.base.BaseDialogFragment
import com.android.appname.ui.base.BaseFragment

fun <T : ViewBinding> BaseActivity.viewBinding(
    bindingInflater: (LayoutInflater) -> T,
    beforeSetContent: () -> Unit = {}
) = ActivityViewBindingDelegate(this, bindingInflater, beforeSetContent)

fun <T : ViewBinding> BaseFragment.viewBinding(
    viewBindingFactory: (View) -> T,
    disposeEvents: T.() -> Unit = {}
) = FragmentViewBindingDelegate(this, viewBindingFactory, disposeEvents)

internal fun ensureMainThread() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        throw IllegalThreadStateException("View can be accessed only on the main thread.")
    }
}
