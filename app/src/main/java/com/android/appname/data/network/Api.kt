package com.android.appname.data.network

import com.example.vuong_project.data.model.request.UserRequest
import com.example.vuong_project.data.model.response.CreateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("user/signup")
    suspend fun createUser(@Body users: UserRequest): Response<CreateUserResponse>
}
