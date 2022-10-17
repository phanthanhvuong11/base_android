package com.android.appname.arch.extensions

import com.android.appname.data.error.ErrorModel
import com.android.appname.data.error.RepositoryException
import com.android.appname.data.network.response.BaseResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
inline fun <T, R> Response<T>.mapSuccess(
    crossinline block: (T) -> R
): R {
    val safeBody = body()
    if (this.isSuccessful && safeBody != null) {
        return block(safeBody)
    } else {
        throw toError()
    }
}

fun <T> Response<T>.mapToRepositoryException(): RepositoryException {
    return RepositoryException(
        code = code(),
        errorBody = errorBody()?.string(),
        msg = message()
    )
}

fun <T> Response<T>.exceptionOnSuccessResponse(): ErrorModel.Http? {
    if (isSuccessful) {
        this.body()?.let { successResponse ->
            if (successResponse is BaseResponse ) {
                return ErrorModel.Http.ApiError(
                    code = successResponse.message,
                    message = successResponse.message,
                    apiUrl = this.raw().request.url.toString()
                )
            }
        }
    }
    return null
}

fun <T> Response<T>.toError(): ErrorModel.Http {
    try {
        return exceptionOnSuccessResponse() ?: ErrorModel.Http.ApiError(
            code = code().toString(),
            message = Json {
                ignoreUnknownKeys = true
            }.decodeFromString<BaseResponse>(errorBody()?.string() ?: "").message
                ?: ErrorModel.LocalErrorException.UN_KNOW_EXCEPTION.message,
            apiUrl = this.raw().request.url.toString()
        )
    } catch (ex: Exception) {
        return ErrorModel.Http.ApiError(
            code = code().toString(),
            message = Json {
                ignoreUnknownKeys = true
            }.decodeFromString<BaseResponse>(errorBody()?.string() ?: "").message
                ?: ErrorModel.LocalErrorException.UN_KNOW_EXCEPTION.message,
            apiUrl = this.raw().request.url.toString()
        )
    }
}

fun Throwable.toError(): ErrorModel {
    return when (this) {
        is SocketTimeoutException -> ErrorModel.LocalError(
            ErrorModel.LocalErrorException.REQUEST_TIME_OUT_EXCEPTION.message,
            ErrorModel.LocalErrorException.REQUEST_TIME_OUT_EXCEPTION.code
        )
        is UnknownHostException -> ErrorModel.LocalError(
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.message,
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.code
        )
        is ConnectException -> ErrorModel.LocalError(
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.message,
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.code
        )
        else -> ErrorModel.LocalError(this.message.toString(), "1014")
    }
}
