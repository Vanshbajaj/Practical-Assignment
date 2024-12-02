@file:Suppress("JavaIoSerializableObjectMustHaveReadResolve")

package com.practical.data.network


sealed class NetworkException : Exception() {
    data object ClientNetworkException : NetworkException()
    data object ApolloClientException : NetworkException()
}





