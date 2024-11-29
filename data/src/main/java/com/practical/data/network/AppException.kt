package com.practical.data.network


open class NetworkException : Exception()

class ClientNetworkException : NetworkException()

class ApolloClientException : NetworkException()


