package com.visiabletech.avision.maharaj.core.retrofit.exception

import com.onlinetalentsearchexam.maharaj.retrofit.exception.ErrorBody

class NoConnectivityException : RuntimeException() {
    companion object {

        val localNetworkErrorBody: ErrorBody
            get() {
                val errorBody = ErrorBody()
                errorBody.code = "500"
                errorBody.message = "No internet connectivity error"
                return errorBody
            }
    }
}