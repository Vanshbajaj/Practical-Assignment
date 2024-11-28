package com.practical.data.network


open class NetworkException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class ClientNetworkException(
    message: String,
    cause: Throwable? = null
) : NetworkException(message, cause)

class ApolloClientException(
    message: String,
    cause: Throwable? = null
) : NetworkException(message, cause)


