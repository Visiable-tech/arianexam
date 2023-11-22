package com.onlinetalentsearchexam.maharaj.retrofit
import com.onlinetalentsearchexam.maharaj.retrofit.exception.NetworkErrorException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class NetworkUtil {
    companion object {
        fun resolveError(e: Throwable): State.ErrorState {
            var error = e

            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkErrorException(errorMessage = "connection error!")
                }
                is ConnectException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
                is UnknownHostException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
            }

            if (e is HttpException) {
                when (e.code()) {









                    else -> error= NetworkErrorException.parseException(e)
                }
            }


            return State.ErrorState(NetworkErrorException(errorMessage = e.localizedMessage))

        }
    }
}
class AuthenticationException(authMessage: String) :
    NetworkErrorException(errorMessage = authMessage)
