package dev.arjundev.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Response<out T> {
    @Serializable
    data class Success<T>(val data: T) : Response<T>()

    @Serializable
    data class Error(
        val exception: String? = null,
        val message: String? = "Something went wrong",
        val reasons:
        List<String>? = emptyList()
    ) : Response<Nothing>()
}

