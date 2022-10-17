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

internal fun Context.showErrorAlert2Button(
    message: String,
    @StringRes buttonLeftTitleRes: Int,
    @StringRes buttonRightTitleRes: Int,
    onLeftBtnClicked: () -> Unit = {},
    onRightBtnClicked: () -> Unit = {},
) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setNegativeButton(getString(buttonLeftTitleRes)) { _, _ ->
        onLeftBtnClicked.invoke()
    }
    builder.setPositiveButton(getString(buttonRightTitleRes)) { _, _ ->
        onRightBtnClicked.invoke()
    }
    builder.create().show()
}

fun Fragment.visibilityFlow(targetFlow: Flow<Boolean>, vararg view: View) {
    collectFlow(targetFlow) { loading ->
        view.forEach { it.isVisible = loading }
    }
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

fun <T1, T2> Fragment.combineFlows(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    collectBlock: ((T1, T2) -> Unit)
) {
    safeViewCollect {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            flow1.combine(flow2) { v1, v2 ->
                collectBlock.invoke(v1, v2)
            }.collect {
                // Empty collect block to trigger ^
            }
        }
    }
}

fun <T1, T2, T3> Fragment.combineFlows(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    collectBlock: ((T1, T2, T3) -> Unit)
) {
    safeViewCollect {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            combine(flow1, flow2, flow3) { v1, v2, v3 ->
                collectBlock.invoke(v1, v2, v3)
            }.collect {
                // Empty collect block to trigger ^
            }
        }
    }
}

fun <T1, T2, T3, T4> Fragment.combineFlows(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    collectBlock: ((T1, T2, T3, T4) -> Unit)
) {
    safeViewCollect {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            combine(flow1, flow2, flow3, flow4) { v1, v2, v3, v4 ->
                collectBlock.invoke(v1, v2, v3, v4)
            }.collect {
                // Empty collect block to trigger ^
            }
        }
    }
}

fun <T1, T2> Fragment.zipFlows(flow1: Flow<T1>, flow2: Flow<T2>, collectBlock: ((T1, T2) -> Unit)) {
    safeViewCollect {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            flow1.zip(flow2) { v1, v2 ->
                collectBlock.invoke(v1, v2)
            }.collect {
                // Empty collect block to trigger ^
            }
        }
    }
}


internal fun FragmentTransaction.animSlideInRightSlideOutRight() {
    setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
}

internal fun FragmentTransaction.animSlideOutRight() {
    setCustomAnimations(R.anim.nothing, 0, 0, R.anim.slide_out_right)
}

internal fun FragmentTransaction.animSlideInUpSlideOutUp() {
    setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_up)
}

internal fun Fragment.popToRoot(keepStackCount: Int = 1) {
    while (childFragmentManager.backStackEntryCount > keepStackCount) {
        childFragmentManager.popBackStackImmediate()
    }
}

internal fun Fragment.getDeviceSettingIntent(): Intent {
    return Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${activity?.packageName}")
    ).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
