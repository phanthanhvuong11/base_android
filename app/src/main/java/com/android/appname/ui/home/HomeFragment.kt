package com.android.appname.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.appname.R
import com.android.appname.arch.extensions.onError
import com.android.appname.arch.extensions.onSuccess
import com.android.appname.arch.extensions.showErrorAlert
import com.android.appname.arch.extensions.viewBinding
import com.android.appname.data.error.ErrorModel
import com.android.appname.databinding.FragmentHomeBinding
import com.android.appname.ui.base.BaseFragment
import com.android.appname.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding(FragmentHomeBinding::bind)

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.tvShow.setOnClickListener {
            viewModel.createUser("vvvvv2v82@gmail.com", "1234567")
                .onSuccess {
                    Log.i("XXXXX", "initViews: ${it}")
                }
                .onError(
                    commonAction = {
                        handleApiCommonError(it)
                    },
                    normalAction = {
                        handleApiCommonError(it)
                    }
                ).launchIn(lifecycleScope)
        }
    }

    override fun getViewModel(): BaseViewModel? = viewModel

    private fun handleApiCommonError(error: ErrorModel) {
        if (error is ErrorModel.Http.ApiError) {
            context?.showErrorAlert(
                message = (error as? ErrorModel.Http.ApiError)?.message.toString(),
                buttonTitleRes = R.string.ok
            )
        } else if (error is ErrorModel.LocalError) {
            context?.showErrorAlert(
                message = (error as? ErrorModel.LocalError)?.errorMessage.toString(),
                buttonTitleRes = R.string.ok
            )
        }
    }
}
