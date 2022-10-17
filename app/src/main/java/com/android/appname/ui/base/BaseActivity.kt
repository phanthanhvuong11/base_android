package com.android.appname.ui.base

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.android.appname.arch.extensions.hideKeyboard
import com.android.appname.data.error.ErrorModel
import com.android.appname.ui.widget.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
@AndroidEntryPoint
abstract class BaseActivity(@LayoutRes layout: Int) : AppCompatActivity(layout) {

    abstract fun afterViewCreated()

    internal fun handleCommonError(errorModel: ErrorModel) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                hideKeyboardWhenTapOutsideEditText(ev).run {
                    return if (this) {
                        this
                    } else {
                        super.dispatchTouchEvent(ev)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboardWhenTapOutsideEditText(ev: MotionEvent): Boolean {
        val view: View? = currentFocus
        if (view is EditText) {
            val rect = Rect()
            view.getGlobalVisibleRect(rect)

            val bound = IntArray(2)
            view.getLocationOnScreen(bound)
            val x = ev.rawX + view.getLeft() - bound[0]
            val y = ev.rawY + view.getTop() - bound[1]

            if (!rect.contains(ev.x.toInt(), ev.y.toInt())
                || x < view.getLeft() || x > view.getRight()
                || y < view.getTop() || y > view.getBottom()
            ) {
                hideKeyboard(this)
                view.clearFocus()
                return true
            }
        }
        return false
    }
}
