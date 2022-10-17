package com.example.vuong_project.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
@Serializable
data class UserRequest(
    @SerialName("email") val email: String? = "",
    @SerialName("password") val password: String? = ""
)
