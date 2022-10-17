package com.android.appname.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.android.appname.arch.extensions.getWidthScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
@AndroidEntryPoint
abstract class BaseDialogFragment(@LayoutRes val layoutId: Int) : DialogFragment(layoutId) {
    companion object {
        private const val WIDTH_RATIO = 0.91f
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogDescription = object : Dialog(requireContext()) {
        }
        dialogDescription.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
            isCancelable = false
        }
        return dialogDescription
    }

    override fun onStart() {
        super.onStart()
        val screenWidth = requireContext().getWidthScreen()
        dialog?.window?.run {
            attributes = attributes.apply {
                width = (screenWidth * WIDTH_RATIO).toInt()
            }
        }
    }
}
