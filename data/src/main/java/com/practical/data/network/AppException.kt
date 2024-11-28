package com.practical.data.network


open class NetworkException(
    cause: Throwable? = null
) : Exception(cause)

class ClientNetworkException(
    cause: Throwable? = null
) : NetworkException(cause)

class ApolloClientException(
    cause: Throwable? = null
) : NetworkException(cause)


