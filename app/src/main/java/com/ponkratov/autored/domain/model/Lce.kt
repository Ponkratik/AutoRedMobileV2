package com.ponkratov.autored.domain.model

sealed class Lce<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Lce<T>(data)
    class Error<T>(message: String?, data: T? = null): Lce<T>(data, message)
    class Loading<T>: Lce<T>()
}