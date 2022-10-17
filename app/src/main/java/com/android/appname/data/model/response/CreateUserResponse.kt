package com.example.vuong_project.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author mvn-vuongphan-dn 10/17/22
 */
@Serializable
data class CreateUserResponse(
    @SerialName("result_code") var resultCode: String? = "",
    @SerialName("detail_code") var detailCode: String? = "",
    @SerialName("message") var message: String? = ""
)
