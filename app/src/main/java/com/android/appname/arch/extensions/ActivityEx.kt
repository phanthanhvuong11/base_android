package com.android.appname.arch.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.appname.ui.base.BaseFragment

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
internal fun AppCompatActivity.replaceFragment(
    @IdRes containerId: Int, fragment: BaseFragment,
    t: (transaction: FragmentTransaction) -> Unit = {},
    isAddBackStack: Boolean = false
) {
    if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.replace(containerId, fragment, fragment.javaClass.simpleName)
        if (isAddBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commitAllowingStateLoss()
    }
}

internal fun AppCompatActivity.addFragment(
    @IdRes containerId: Int, fragment: BaseFragment,
    t: (transaction: FragmentTransaction) -> Unit = {}, backStackString: String? = null
) {
    if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.add(containerId, fragment, fragment.javaClass.simpleName)
        if (backStackString != null) {
            transaction.addToBackStack(backStackString)
        }
        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }
}

internal fun AppCompatActivity.addFragment(
    @IdRes containerId: Int, fragment: BaseFragment,
    t: (transaction: FragmentTransaction) -> Unit = {}, backStackString: String? = null,
    tag: String
) {
    if (supportFragmentManager.findFragmentByTag(tag) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.add(containerId, fragment, tag)
        if (backStackString != null) {
            transaction.addToBackStack(backStackString)
        }
        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }
}

internal fun AppCompatActivity.getCurrentFragment(@IdRes containerId: Int) =
    supportFragmentManager.findFragmentById(containerId)

internal fun AppCompatActivity.popFragment() {
    supportFragmentManager.popBackStackImmediate()
}

internal fun AppCompatActivity.clearBackStack() {
    supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}
