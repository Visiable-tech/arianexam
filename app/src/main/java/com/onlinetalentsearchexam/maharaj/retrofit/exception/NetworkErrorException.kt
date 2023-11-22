
package com.onlinetalentsearchexam.maharaj.retrofit.exception

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

open class NetworkErrorException(
    val errorCode: Int = -1,
    val errorMessage: String,
    val response: String = ""
) : Exception() {
    override val message: String
        get() = localizedMessage

    override fun getLocalizedMessage(): String {
        return errorMessage
    }

    companion object {
        fun parseException(e: HttpException): NetworkErrorException {
            val body = e.response()?.errorBody()?.string()

            var msg="Unknown!"

            return try {
                if(!body.isNullOrEmpty())
                    msg=JSONObject(JSONObject(body!!).getString("errors")).getString("message")

                Log.d("TAG",msg)
                NetworkErrorException(e.code(), msg)
            } catch (_: Exception) {
                NetworkErrorException(e.code(), "unexpected error!!ً")
            }
        }
    }
}

class AuthenticationException(authMessage: String) :
    NetworkErrorException(errorMessage = authMessage)
