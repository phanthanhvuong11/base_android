package com.android.appname.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
@Serializable
data class BaseResponse(
    @SerialName("message") val message: String? = ""
)
