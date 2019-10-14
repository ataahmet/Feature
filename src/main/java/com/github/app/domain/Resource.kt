package com.github.app.domain

sealed class Resource<out D>

class Loading<out D> : Resource<D>()
data class Success<out D>(val data: D? = null) : Resource<D>()
sealed class Error<out D> : Resource<D>()

class UnknownError<out D> : Error<D>()
class NetworkError<out D> : Error<D>()
class ServerError<out D> : Error<D>()
class UnauthorizedError<out D> : Error<D>()
data class MessageError<out D>(val message: String) : Error<D>()
data class FormError<out D>(val message: String, val errors: Map<String, List<String>>) :
    Error<D>()