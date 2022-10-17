package com.android.appname.ui.home

import android.util.Log
import androidx.lifecycle.bindCommonError
import androidx.lifecycle.bindLoading
import com.android.appname.arch.extensions.FlowResult
import com.android.appname.arch.extensions.onError
import com.android.appname.ui.base.BaseViewModel
import com.example.vuong_project.data.model.response.CreateUserResponse
import com.example.vuong_project.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel(), HomeVMContract {

    override fun createUser(email: String, password: String): Flow<FlowResult<CreateUserResponse>> =
        authRepository.createUser(email, password)
            .bindCommonError(this)
            .bindLoading(this)
            .onError(
                normalAction = {
                    Log.i("XXXXX", "createUser1: ${it}")
                },
                commonAction = {
                    Log.i("XXXX", "createUser:2 ${it.isCommonError()}")
                    Log.i("xxxxx", "createUser: 3${it.message}")
                }
            )
}
