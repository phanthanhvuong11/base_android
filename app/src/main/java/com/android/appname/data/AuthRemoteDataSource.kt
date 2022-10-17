package com.android.appname.data

import com.android.appname.arch.extensions.apiCall
import com.android.appname.data.network.Api
import com.example.vuong_project.data.model.request.UserRequest
import com.example.vuong_project.data.model.response.CreateUserResponse
import javax.inject.Inject

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
class AuthRemoteDataSource @Inject constructor(
    private val api: Api
) {

    suspend fun createUser(email: String, password: String): CreateUserResponse = apiCall {
        api.createUser(UserRequest(email, password))
    }
}
