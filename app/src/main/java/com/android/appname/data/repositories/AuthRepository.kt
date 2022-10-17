package com.example.vuong_project.data.repositories

import com.android.appname.arch.data.Repository
import com.android.appname.arch.extensions.FlowResult
import com.android.appname.arch.extensions.safeFlow
import com.android.appname.data.AuthRemoteDataSource
import com.example.vuong_project.data.model.response.CreateUserResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : Repository() {

    fun createUser(email: String, password: String): Flow<FlowResult<CreateUserResponse>> =
        safeFlow {
            authRemoteDataSource.createUser(email, password)
        }
}
