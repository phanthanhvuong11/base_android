package com.android.appname.arch.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.android.appname.R
import com.android.appname.ui.base.BaseFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
internal fun Fragment.addChildFragment(
    @IdRes containerId: Int, fragment: BaseFragment, backStack: String? = null,
    t: (transaction: FragmentTransaction) -> Unit = {}
) {
    if (childFragmentManager.findFragmentByTag(backStack) == null) {
        val transaction = childFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.add(containerId, fragment, fragment.javaClass.name)
        if (backStack != null) {
            transaction.addToBackStack(backStack)
        }
        transaction.commitAllowingStateLoss()
    }
}

internal fun Fragment.popChildFragment() {
    childFragmentManager.popBackStack()
}

internal fun Fragment.replaceChildFragment(
    @IdRes containerId: Int, fragment: BaseFragment, backStack: String? = null,
    t: (transaction: FragmentTransaction) -> Unit = {}
) {
    val transaction = childFragmentManager.beginTransaction()
    t.invoke(transaction)
    transaction.replace(containerId, fragment, backStack)
    if (backStack != null) {
        transaction.addToBackStack(backStack)
    }
    transaction.commit()
}

internal fun Fragment.getCurrentFragment(@IdRes containerId: Int): Fragment? {
    return childFragmentManager.findFragmentById(containerId)
}

internal fun Context.showErrorAlert(
    message: String,
    @StringRes buttonTitleRes: Int,
    onOkClicked: () -> Unit = {}
) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setNegativeButton(getString(buttonTitleRes)) { _, _ ->
        onOkClicked.invoke()
    }
    builder.create().show()
}
fun <T> Fragment.collectFlow(targetFlow: Flow<T>, collectBlock: ((T) -> Unit)) {
    safeViewCollect {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            targetFlow.collect {
                collectBlock.invoke(it)
            }
        }
    }
}

private inline fun Fragment.safeViewCollect(crossinline viewOwner: LifecycleOwner.() -> Unit) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            viewLifecycleOwnerLiveData.observe(
                this@safeViewCollect,
                { viewLifecycleOwner ->
                    viewLifecycleOwner.viewOwner()
                }
            )
        }
    })
}
