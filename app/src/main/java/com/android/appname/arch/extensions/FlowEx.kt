package com.android.appname.arch.extensions

import com.android.appname.data.error.ErrorModel
import kotlinx.coroutines.flow.*

sealed class FlowResult<out T> {
    data class Success<T>(val value: T) : FlowResult<T>()
    data class Error(val error: ErrorModel) : FlowResult<Nothing>()
}

inline fun <T> safeFlow(
    crossinline block: suspend () -> T,
): Flow<FlowResult<T>> = flow {
    try {
        val repoResult = block()
        emit(FlowResult.Success(repoResult))
    } catch (e: ErrorModel) {
        emit(FlowResult.Error(e))
    } catch (e: Exception) {
        e.printStackTrace()
        emit(FlowResult.Error(e.toError()))
    }
}

fun <T> Flow<FlowResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Success<T>) {
            action(result.value)
        }
        return@transform emit(result)
    }

fun <T> Flow<FlowResult<T>>.onError(
    normalAction: suspend (ErrorModel) -> Unit = {},
    commonAction: suspend (ErrorModel) -> Unit = {}
): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Error) {
            if (!result.error.isCommonError()) {
                normalAction(result.error)
            } else {
                commonAction(result.error)
            }
        }
        return@transform emit(result)
    }
