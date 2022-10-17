package com.android.appname.ui.home

import com.android.appname.arch.extensions.FlowResult
import com.example.vuong_project.data.model.response.CreateUserResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
interface HomeVMContract {

    fun createUser(email: String, password: String): Flow<FlowResult<CreateUserResponse>>
}
