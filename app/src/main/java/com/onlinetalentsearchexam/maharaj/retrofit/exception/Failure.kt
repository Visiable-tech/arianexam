package com.onlinetalentsearchexam.maharaj.retrofit.exception

import retrofit2.HttpException


@Suppress("UNUSED_PARAMETER")
sealed class Failure {
    object NetworkConnection : Failure()
    class HttpError(val throwable: HttpException) : Failure()
    object NetworkException : Failure()
    class ServerError(val code : Int, val errorBody : ErrorBody) : Failure()
    object IOException : Failure()
    object IllegalStateException : Failure()
    class AccountMisMatchException( userIdentifier : String) : Failure()
    class UnKnownException(val throwable: Throwable) : Failure()
    object UnKnownDataLoadException : Failure()


        abstract class FeatureFailure : Failure()
}