package com.android.appname.ui.widget

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.android.appname.R

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
class CustomProgressDialog : DialogFragment() {

    companion object {
        @Volatile
        private var isShowing = false

        fun newInstance(): CustomProgressDialog = CustomProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_progress_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShowing) {
            if (isAdded) {
                manager.findFragmentByTag(tag)?.also {
                    val transaction = manager.beginTransaction()
                    transaction.remove(it)
                    transaction.commitNowAllowingStateLoss()
                }
            }
            isShowing = true
            try {
                super.show(manager, tag)
            } catch (e: Exception) {
                manager.beginTransaction().also {
                    it.add(this, tag)
                    it.commitNowAllowingStateLoss()
                }
            }
        }
    }

    override fun dismissAllowingStateLoss() {
        isShowing = try {
            super.dismissAllowingStateLoss()
            false
        } catch (e: Exception) {
            false
        }
    }

    override fun onDestroyView() {
        isShowing = false
        super.onDestroyView()
    }

    private fun setupDialog() {
        dialog?.run {
//            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
            isCancelable = false
            setCanceledOnTouchOutside(false)
        }
    }
}
