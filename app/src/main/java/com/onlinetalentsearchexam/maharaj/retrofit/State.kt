
package com.onlinetalentsearchexam.maharaj.retrofit

import com.onlinetalentsearchexam.maharaj.retrofit.exception.NetworkErrorException

sealed class State<out T : Any> {
    data class LoadingState(val isLoading: Boolean) : State<Nothing>()
    data class ErrorState(var exception: NetworkErrorException) : State<Nothing>()
    data class DataState<T : Any>(var data: T) : State<T>()
}
