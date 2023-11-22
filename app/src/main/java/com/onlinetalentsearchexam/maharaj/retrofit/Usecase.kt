package com.onlinetalentsearchexam.maharaj.retrofit

import com.onlinetalentsearchexam.maharaj.retrofit.exception.Failure
import com.onlinetalentsearchexam.maharaj.retrofit.exception.ErrorBody
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

abstract class Usecase<out Type, in Params>() where Type: Any? {

    @Inject
    lateinit var networkHandler: NetworkHandler

     var job: Job? = null

    abstract suspend fun run(): Result<Type>

    fun execute(onResult: (Result<Type>) -> Unit, params: Params) {
        job = CoroutineScope(Dispatchers.IO).launch {

            when(networkHandler.isConnected) {
                true -> {
                    onResult.invoke(run())
                }

                false, null -> {
                    onResult.invoke(Error(failure = Failure.NetworkConnection))
                }
            }
        }
    }

    protected fun getFailure(response: Response<Any>): Failure {
        return when (response.code()) {
            401 -> Failure.AccountMisMatchException("")
            else -> {

                var errorBody = ErrorBody()
                errorBody.code = response.code().toString()
                errorBody.message = response.message()

                val error = response.errorBody()?.charStream()

                if (error != null) {
                    try {
                        errorBody = Gson().fromJson(error, ErrorBody::class.java)
                    } catch (e: Exception) {

                    }
                }
                Failure.ServerError(response.code(), errorBody)
            }
        }
    }

    protected fun getFailureError(errorCode: Int, response: ResponseBody?): Failure {
        var errorBody = ErrorBody()
        try {
            response?.let {
                errorBody = Gson().fromJson(it.charStream(), ErrorBody::class.java)
            }
        } catch (e: Exception) {

        }
        return Failure.ServerError(errorCode, errorBody)
    }


    class None


    protected fun getFailureException(throwable: Throwable): Failure {
        throwable.printStackTrace()
        return when (throwable) {
            is IOException -> Failure.NetworkException
            is HttpException -> Failure.HttpError(throwable)
            else -> Failure.UnKnownException(throwable)
        }
    }

        fun cancelUseCase() {
        job?.run {
            if (isActive) {
                cancel()
            }
        }

    }

    fun isUseCaseCompleted(): Boolean {
        return job?.isCompleted ?: true
    }
}