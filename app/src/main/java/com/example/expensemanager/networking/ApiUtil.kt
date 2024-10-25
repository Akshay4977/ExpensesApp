package com.example.expensemanager.networking

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

object ApiUtil {

    suspend fun <T> makeApiCall(
        dispatcher: CoroutineDispatcher,
        call: suspend () -> T

    ): Result<T> = runCatching {
        withContext(dispatcher) {
            call.invoke()
        }
    }


    fun <T> handleResponse(response: Response<T>): T {
        when {
            (response.isSuccessful && response.body() != null) -> {
                return response.body()!!
            }

            else -> {
                throw Throwable("Throwable")
            }
        }
    }
}
